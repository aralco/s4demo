package com.s4.demo.service;

import com.s4.demo.domain.Clazz;
import com.s4.demo.domain.Student;
import com.s4.demo.repository.StudentRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.bind.ValidationException;
import java.util.*;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ClazzService clazzService;

    public Student create(Student student) throws ValidationException, NotFoundException {
        //Control of a single studentId
        try {
            findByStudentId(student.getStudentId());
            throw new ValidationException(String.format("One student with studentId '%s' alredy exist.", student.getStudentId()));
        }catch (NotFoundException e){
            return studentRepository.save(student);
        }

    }

    public Student update(Student student, String studentId) throws NotFoundException, ValidationException {

        Student persistedStudent = studentRepository.findByStudentId(studentId);

        if( student==null ){
            throw new NotFoundException(String.format("The student with id %s not found.", studentId));
        }

        //Control of a single studentId
        if(!student.getStudentId().equals(studentId)
                && findByStudentId(student.getStudentId())!=null){
            throw new ValidationException(String.format("One student with studentId '%s' alredy exist.", student.getStudentId()));
        }

        persistedStudent.setStudentId(student.getStudentId());
        persistedStudent.setFirstName(student.getFirstName());
        persistedStudent.setLastName(student.getLastName());
        persistedStudent = studentRepository.save(persistedStudent);

        return persistedStudent;
    }

    public Student findByStudentId(String studentId) throws NotFoundException {

        Student student = studentRepository.findByStudentId(studentId);
        if( student==null ){
            throw new NotFoundException(String.format("The student with id %s not found.", studentId));
        }

        return student;
    }

    @Transactional
    public Student addClass(String studentId, String classCode) throws NotFoundException {
        Student student = findByStudentId(studentId);
        if( student==null ){
            throw new NotFoundException(String.format("The student with id %s not found.", studentId));
        }
        Clazz clazz = clazzService.getClazzByCode(classCode);

        if( clazz==null ){
            throw new NotFoundException(String.format("The Class with code %s not found.", classCode));
        }

        student.addClazz(clazz);
        student = studentRepository.save(student);

        return student;
    }

    public List<Student> getAll() {
        List<Student> res = studentRepository.findAll();
        return res;
    }

    public List<Student> findAll(Specification<Student> specification) {
        return studentRepository.findAll(specification);
    }

    public Set<Clazz> getAllClazz(String studentId) throws NotFoundException {
        Student student = studentRepository.findByStudentId(studentId);
        if( student==null ){
            throw new NotFoundException(String.format("The student with id %s not found.", studentId));
        }
        Set<Clazz> clazzes = new HashSet<>();

        if( student.getClass()!=null ){
            clazzes = student.getClazzes();
        }
        return clazzes;
    }

    public void removeByStudentId(String studentId) throws NotFoundException {

        Student student = studentRepository.findByStudentId(studentId);

        if( student==null ){
            throw new NotFoundException(String.format("The student with id %s not found.", studentId));
        }

        studentRepository.delete(student);
    }

}
