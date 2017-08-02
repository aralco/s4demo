package com.s4.demo.rest;

import com.s4.demo.domain.search.SearchCriteria;
import com.s4.demo.domain.Student;
import com.s4.demo.domain.search.SpecificationsBuilder;
import com.s4.demo.rest.model.StudentRequest;
import com.s4.demo.rest.model.StudentResponse;
import com.s4.demo.service.ClazzService;
import com.s4.demo.service.StudentService;
import io.swagger.annotations.Api;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

@Api
@RestController
@RequestMapping("/students")
public class StudentController extends GenericController<Student, StudentRequest> {

    @Autowired
    private StudentService service;

    @Autowired
    private ClazzService clazzService;

    @PostMapping
    public ResponseEntity<StudentResponse> create(@RequestBody StudentRequest request) throws NotFoundException, ValidationException {
        Student student = modelMapper.map(request, Student.class);
        return ResponseEntity
                .ok(modelMapper.map(service.create(student), StudentResponse.class));
    }

    @GetMapping
    public ResponseEntity<List<StudentResponse>> getAll() {
        List<StudentResponse> responses = service.getAll().stream().map(post -> modelMapper.map(post, StudentResponse.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{studentId}")
    public ResponseEntity<StudentResponse> update(@PathVariable String studentId, @RequestBody StudentRequest request) throws NotFoundException, ValidationException {
        Student student = modelMapper.map(request, Student.class);
        return ResponseEntity
                .ok(modelMapper.map(service.update(student, studentId), StudentResponse.class));
    }

    @GetMapping("/{classCode}/students")
    public ResponseEntity<List<StudentResponse>> getAllStudentsByClassCode(@PathVariable String classCode) throws NotFoundException {
        List<StudentResponse> responses = clazzService.getAllStudents(classCode).stream().map(post -> modelMapper.map(post, StudentResponse.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/class/{studentId}")
    public ResponseEntity<StudentResponse> update(@PathVariable String studentId, @RequestBody String classCode) throws NotFoundException {
        Student student = service.addClass(studentId, classCode);
        return ResponseEntity
                .ok(modelMapper.map(student, StudentResponse.class));
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<String> delete(@PathVariable String studentId) throws NotFoundException {
        service.removeByStudentId(studentId);
        return ResponseEntity
                .ok(String.format("The student with id '%s' was successfully deleted.", studentId));
    }

    @PostMapping("/search")
    public ResponseEntity<List<StudentResponse>> search(@RequestBody List<SearchCriteria> search) {
        SpecificationsBuilder<Student> builder = new SpecificationsBuilder<Student>();

       for(SearchCriteria criteria: search){
           String operator = criteria.getOperation().trim().equals("=")
                   ?":":criteria.getOperation().trim();
           builder.with(criteria.getKey(), operator, criteria.getValue());
       }

        Specification<Student> spec = builder.build();

        List<StudentResponse> responses = service.findAll(spec).stream().map(post -> modelMapper.map(post, StudentResponse.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

}
