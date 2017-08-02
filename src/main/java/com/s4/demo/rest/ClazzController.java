package com.s4.demo.rest;

import com.s4.demo.domain.Clazz;
import com.s4.demo.domain.Student;
import com.s4.demo.domain.search.SearchCriteria;
import com.s4.demo.domain.search.SpecificationsBuilder;
import com.s4.demo.rest.model.ClazzRequest;
import com.s4.demo.rest.model.ClazzResponse;
import com.s4.demo.rest.model.StudentRequest;
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
@RequestMapping("/classes")
public class ClazzController extends GenericController<Student, StudentRequest> {

    @Autowired
    private ClazzService clazzService;

    @Autowired
    private StudentService studentService;

    @PostMapping
    public ResponseEntity<ClazzResponse> create(@RequestBody ClazzRequest request) throws NotFoundException, ValidationException {
        Clazz clazz = modelMapper.map(request, Clazz.class);
        return ResponseEntity
                .ok(modelMapper.map(clazzService.create(clazz), ClazzResponse.class));
    }

    @GetMapping
    public ResponseEntity<List<ClazzResponse>> getAll() {
        List<ClazzResponse> responses = clazzService.getAll().stream().map(post -> modelMapper.map(post, ClazzResponse.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{studentId}/classes")
    public ResponseEntity<List<ClazzResponse>> getAllClazzbyStudentId(@PathVariable String studentId) throws NotFoundException {
        List<ClazzResponse> responses = studentService.getAllClazz(studentId).stream().map(post -> modelMapper.map(post, ClazzResponse.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{code}")
    public ResponseEntity<ClazzResponse> update(@PathVariable String code, @RequestBody ClazzRequest request) throws NotFoundException, ValidationException {
        Clazz clazz = modelMapper.map(request, Clazz.class);
        return ResponseEntity
                .ok(modelMapper.map(clazzService.update(clazz, code), ClazzResponse.class));
    }

    @PutMapping("/student/{classCode}")
    public ResponseEntity<ClazzResponse> update(@PathVariable String classCode, @RequestBody String studentId) throws NotFoundException {
        Clazz clazz = clazzService.addStudent(classCode, studentId);
        return ResponseEntity
                .ok(modelMapper.map(clazz, ClazzResponse.class));
    }

    @DeleteMapping("/{classCode}")
    public ResponseEntity<String> delete(@PathVariable String classCode) throws NotFoundException {
        clazzService.removeByClazzCode(classCode);
        return ResponseEntity
                .ok(String.format("The Class with code '%s' was successfully deleted.", classCode));
    }

    @PostMapping("/search")
    public ResponseEntity<List<ClazzResponse>> search(@RequestBody List<SearchCriteria> search) {
        SpecificationsBuilder<Clazz> builder = new SpecificationsBuilder<Clazz>();

        for(SearchCriteria criteria: search){
            String operator = criteria.getOperation().trim().equals("=")
                    ?":":criteria.getOperation().trim();
            builder.with(criteria.getKey(), operator, criteria.getValue());
        }

        Specification<Clazz> spec = builder.build();

        List<ClazzResponse> responses = clazzService.findAll(spec).stream().map(post -> modelMapper.map(post, ClazzResponse.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

}
