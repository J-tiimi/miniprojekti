package com.j.tiimi.controller;

import com.google.gson.Gson;
import com.j.tiimi.entity.Attribute;
import com.j.tiimi.entity.Reference;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReferenceControllerTest {

    @Autowired
    WebApplicationContext webAppContext;

    private MockMvc mockMvc;
    private Gson gson;
    private String bookJson;
    private String invalidBookJson;
    private String inproceedingsJson;
    private String invalidInproceedingsJson;
    private String articleJson;
    private String invalidArticleJson;

    private String invalidTypeReferenceJson;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
        gson = new Gson();

        bookJson = gson.toJson(generateBookReference(
                "Seppo", "Sepon Seikkaulut", "ACM", "1977", "SS1977"));
        invalidBookJson = gson.toJson(generateBookReference(
                "Seppo", "Sepon Seikkaulut", "ACM", "", ""));

        inproceedingsJson = gson.toJson(generateInproceedingsReference(
                "William Stallings", "Writing the worst os book ever", "Asdf", "1999", "Identifaier"));
        invalidInproceedingsJson = gson.toJson(generateInproceedingsReference(
                "", "Writing the worst os book ever", "Asdf", "1999", "identifaier"));

        articleJson = gson.toJson(generateArticleReference(
                "asdfmies", "asdfhdsjkf", "asdfasdf", "1999", "asdfsdf", "uniikki"));
        invalidArticleJson = gson.toJson(generateArticleReference(
                "asdfmies", "asdfhdsjkf", "fdf", "", "", "asdf"));

        invalidTypeReferenceJson = gson.toJson(
                new Reference("Asdftyyppi", "Identifier", new ArrayList<>())
        );
    }

    // TODO Testaus incopreecedings ja article tyypeille

    @Test
    public void statusOk() throws Exception {
        mockMvc.perform(get("/reference"))
                .andExpect(status().isOk());
    }

    @Test
    public void postFailsWithInvalidReferenceType() {
        try {
            mockMvc.perform(post("/reference")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(invalidTypeReferenceJson)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        } catch (Exception e) {
            Assert.fail("Fail " + e);
        }
    }

    @Test
    public void postFailsWithNoJsonBody() {
        try {
            mockMvc.perform(post("/reference")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("")
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        } catch (Exception e) {
            Assert.fail("Fail: " + e);
        }
    }

    @Test
    public void postSucceedsWithValidBookReference() {
        try {
            mockMvc.perform(post("/reference")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(bookJson)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            Assert.fail("Fail: " + e);
        }
    }

    @Test
    public void postFailsWithInvalidBookReference() {
        try {
            mockMvc.perform(post("/reference")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(invalidBookJson)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        } catch (Exception e) {
            Assert.fail("Fail " + e);
        }
    }

    @Test
    public void postSucceedsWithValidInproceedingsReference() {
       try {
           mockMvc.perform(post("/reference")
                   .contentType(MediaType.APPLICATION_JSON)
                   .content(inproceedingsJson)
                   .accept(MediaType.APPLICATION_JSON))
                   .andExpect(status().isOk());
       } catch (Exception e) {
           Assert.fail("Fail " + e);
       }
    }
    @Test
    public void postFailsWithInvalidInproceedingsReference() {
        try {
            mockMvc.perform(post("/reference")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(invalidInproceedingsJson)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        } catch (Exception e) {
            Assert.fail("Fail " + e);
        }
    }

    @Test
    public void postSucceedsWithValidArticleReference() {
        try {
            mockMvc.perform(post("/reference")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(articleJson)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            Assert.fail("Fail " + e);
        }
    }

    @Test
    public void postFailsWithInvalidArticleReference() {
        try {
            mockMvc.perform(post("/reference")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(invalidArticleJson)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        } catch (Exception e) {
            Assert.fail("Fail " + e);
        }
    }

    @Test
    public void deleteSuccessful() {
        try {
            mockMvc.perform(delete("/reference/1")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            Assert.fail("Fail " + e);
        }
    }

    public Reference generateBookReference(String... values) {
        List<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute("author", values[0]));
        attributes.add(new Attribute("title", values[1]));
        attributes.add(new Attribute("publisher", values[2]));
        attributes.add(new Attribute("year", values[3]));

        return new Reference("Book", values[4], attributes);
    }

    public Reference generateInproceedingsReference(String... values) {
        List<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute("author", values[0]));
        attributes.add(new Attribute("title", values[1]));
        attributes.add(new Attribute("booktitle", values[2]));
        attributes.add(new Attribute("year", values[3]));

        return new Reference("Inproceedings", values[4], attributes);
    }

    /*
            "AUTHOR",
            "TITLE",
            "JOURNAL",
            "YEAR",
            "VOLUME"

     */

    public Reference generateArticleReference(String... values) {
        List<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute("author", values[0]));
        attributes.add(new Attribute("title", values[1]));
        attributes.add(new Attribute("journal", values[2]));
        attributes.add(new Attribute("year", values[3]));
        attributes.add(new Attribute("volume", values[4]));

        return new Reference("Article", values[5], attributes);
    }
}
