package com.s4.demo;

import com.s4.demo.domain.Clazz;
import com.s4.demo.domain.Student;
import com.s4.demo.repository.ClazzRepository;
import com.s4.demo.repository.StudentRepository;
import com.s4.demo.service.ClazzService;
import com.s4.demo.service.StudentService;
import javassist.NotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.xml.bind.ValidationException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClazzServiceTest {

    @Autowired
    private StudentService studentService;

    @Autowired
    private ClazzService clazzService;

    @Autowired
    private ClazzRepository clazzRepository;

    @Autowired
    private StudentRepository studentRepository;

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    Student dummyStudent;

    Clazz dummyClazz;

    @Before
    public void setup() throws NotFoundException, ValidationException {

        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        dummyStudent = new Student();
        dummyStudent.setFirstName("jhon");
        dummyStudent.setLastName("Deep");
        dummyStudent.setStudentId("1");
        dummyStudent.setId(1L);

        dummyClazz = new Clazz();
        dummyClazz.setCode("001");
        dummyClazz.setDescription("test");
        dummyClazz.setTitle("math");
    }


    @Test
    public void addClassToStudent() throws Exception {

        dummyClazz = clazzService.create(dummyClazz);
        assertNotNull(dummyClazz);

        dummyStudent = studentService.create(dummyStudent);
        assertNotNull(dummyStudent);


        Clazz resp = clazzService.addStudent(dummyClazz.getCode(), dummyStudent.getStudentId());

        assertEquals(resp.getStudents().iterator().next(), dummyStudent);

    }

}
