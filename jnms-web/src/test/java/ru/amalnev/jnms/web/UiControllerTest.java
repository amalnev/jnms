package ru.amalnev.jnms.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.amalnev.jnms.common.model.entities.DisplayName;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:test.properties")
@AutoConfigureMockMvc
@SpringBootTest
public class UiControllerTest
{
    @Autowired
    private MockMvc mvc;

    @Autowired
    private EntityManager entityManager;

    private void testGridPage(final String entityClassName) throws Exception
    {
        mvc.perform(get("/grid?entityClassName=" + entityClassName).contentType(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("columnNames"))
                .andExpect(model().attributeExists("columnValues"))
                .andExpect(model().attribute("viewType", "grid"));
    }

    @Test
    @WithUserDetails("root")
    public void testPages() throws Exception
    {
        for(final EntityType<?> entityType : entityManager.getMetamodel().getEntities())
        {
            final Class entityClass = Class.forName(entityType.getJavaType().getCanonicalName());
            if (entityClass.isAnnotationPresent(DisplayName.class))
            {
                testGridPage(entityClass.getName());
            }
        }
    }
}
