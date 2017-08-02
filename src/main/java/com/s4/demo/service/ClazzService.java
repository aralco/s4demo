package com.s4.demo.service;

import com.s4.demo.domain.Clazz;
import com.s4.demo.domain.Student;
import com.s4.demo.repository.ClazzRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.bind.ValidationException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ClazzService {
    @Autowired
    private ClazzRepository clazzRepository;

    @Autowired
    private StudentService studentService;

    public Clazz create(Clazz clazz) throws ValidationException, NotFoundException {

        try {
            getClazzByCode(clazz.getCode());
            throw new ValidationException(String.format("One class with code '%s' alredy exist.", clazz.getCode()));
        }catch (NotFoundException e){
            return clazzRepository.save(clazz);
        }

    }

    public Clazz update(Clazz clazz, String code) throws NotFoundException, ValidationException {
        Clazz persistedClazz = clazzRepository.findByCode(code);

        if( persistedClazz==null ){
            throw new NotFoundException(String.format("The 'Class' with code %s not found.", code));
        }

        //Control of a single studentId
        if(!clazz.getCode().equals(code)
                && getClazzByCode(clazz.getCode())!=null){
            throw new ValidationException(String.format("One 'Class' with code '%s' alredy exist.", clazz.getCode()));
        }

        if(persistedClazz !=null){
            persistedClazz.setDescription(clazz.getDescription());
            persistedClazz.setCode(clazz.getCode());
            persistedClazz.setTitle(clazz.getTitle());
            persistedClazz = clazzRepository.save(persistedClazz);
        }
        return persistedClazz;
    }

    @Transactional
    public Clazz addStudent(String classCode, String studentId) throws NotFoundException {
        Student student = studentService.findByStudentId(studentId);
        if( student==null ){
            throw new NotFoundException(String.format("The student with id %s not found.", studentId));
        }
        Clazz clazz = clazzRepository.findByCode(classCode);

        if( clazz==null ){
            throw new NotFoundException(String.format("The Class with code %s not found.", classCode));
        }

        clazz.addStudent(student);
        clazz = clazzRepository.save(clazz);
        return clazz;
    }

    public Set<Student> getAllStudents(String classCode) throws NotFoundException {
        Clazz clazz = clazzRepository.findByCode(classCode);
        if( clazz==null ){
            throw new NotFoundException(String.format("The Class with code %s not found.", classCode));
        }
        Set<Student> students = new HashSet<>();

        if( clazz.getStudents()!=null ){
            students = clazz.getStudents();
        }
        return students;
    }

    public List<Clazz> getAll() {
        List<Clazz> res = clazzRepository.findAll();
        return res;
    }

    public Clazz getClazzByCode(String code) throws NotFoundException {

        Clazz clazz = clazzRepository.findByCode(code);
        if( clazz==null ){
            throw new NotFoundException(String.format("The Class with code %s not found.", code));
        }
        return clazz;
    }

    public void removeByClazzCode(String code) throws NotFoundException {

        Clazz clazz = clazzRepository.findByCode(code);

        if( clazz==null ){
            throw new NotFoundException(String.format("The Class with code %s not found.", code));
        }

        clazzRepository.delete(clazz);
    }

    public List<Clazz> findAll(Specification<Clazz> specification) {
        return clazzRepository.findAll(specification);
    }
}
