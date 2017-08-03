package com.s4.demo;

import com.s4.demo.domain.Clazz;
import com.s4.demo.domain.Student;
import com.s4.demo.rest.StudentController;
import com.s4.demo.service.ClazzService;
import com.s4.demo.service.StudentService;
import javassist.NotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.xml.bind.ValidationException;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class StudentControllerTest {

    @Mock
    private StudentService studentService;

    @Mock
    private ClazzService clazzService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private StudentController studentController;

    private MockMvc mockMvc;

    @Before
    public void setup() throws NotFoundException, ValidationException {

        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();

        Student dummyStudent = new Student();
        dummyStudent.setFirstName("jhon");
        dummyStudent.setLastName("Deep");
        dummyStudent.setStudentId("1");
        dummyStudent.setId(1L);
        Mockito.when(
                studentService.create(
                        Mockito.any(Student.class))).thenReturn(dummyStudent);

        Clazz dummyClazz = new Clazz();
        dummyClazz.setCode("001");
        dummyClazz.setDescription("test");
        dummyClazz.setTitle("math");
        Mockito.when(
                clazzService.create(
                        Mockito.any(Clazz.class))).thenReturn(dummyClazz);

        dummyStudent.addClazz(dummyClazz);
        Mockito.when(
                studentService.addClass(
                        Mockito.anyString(), Mockito.anyString())).thenReturn(dummyStudent);
    }

    String studentJson = String.format(
            "{ \"firstName\": \"%s\", " +
                    "  \"lastName\": \"%s\", " +
                    "  \"studentId\": \"%s\"  }"
            , "jhon", "Deep", "1");

    private final String CRETE_STUDENT_URL = "/students";

    @Test
    public void createStudent() throws Exception {

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(CRETE_STUDENT_URL)
                .accept(MediaType.APPLICATION_JSON).content(studentJson)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());

        assertEquals(CRETE_STUDENT_URL,
                response.getHeader(HttpHeaders.LOCATION));
    }

    @Test
    public void addClassToStudent() throws Exception {

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/students/class/1")
                .accept(MediaType.APPLICATION_JSON).content("001")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

    }

}
