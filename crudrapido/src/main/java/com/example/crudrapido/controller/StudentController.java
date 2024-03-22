package com.example.crudrapido.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

import com.example.crudrapido.entity.Student;
import com.example.crudrapido.service.StudentService;

@Controller
public class StudentController {

    @Autowired
    private StudentService studentService;

    // listar estudiantes
    @RequestMapping({ "/students", "/" })
    public String students(Model model) {
        Student student = new Student();
        model.addAttribute("students", studentService.getStudents());
        model.addAttribute("findStudent", student);
        return "students"; // devolver la parte visual
    }

    // agregar estudiante
    @GetMapping("/students/nuevo")
    public String crearStudentForm(Model model) {
        Student student = new Student();
        model.addAttribute("student", student);
        return "crear_student";
    }

    // Registrar
    @PostMapping("/students")
    public String guardarStudent(@ModelAttribute("student") Student student) {
        studentService.saveOrUpdate(student);
        return "redirect:/students";
    }

    // consultar estudiante por id para editar
    @GetMapping("/students/editar/{id}")
    public String getBId(@PathVariable("id") Long id, Model model) {
        model.addAttribute("student", studentService.getStudent(id));
        return "student_edit";
    }

    // actualizar guarda los cambios en la tabla student
    @PostMapping("/students/update/{id}")
    public String actualizarStudent(@PathVariable Long id, @ModelAttribute("student") Student student) {
        try {
            Student studentEdit = studentService.getStudent(id); // buscar el estudiante
            // modifica la informacion
            studentEdit.setStudentId(id);
            studentEdit.setFirstName(student.getFirstName());
            studentEdit.setLastName(student.getLastName());
            studentEdit.setEmail(student.getEmail());

            // guardar los datos nuevos
            studentService.saveOrUpdate(studentEdit);

            // redirir al inicio
            return "redirect:/students";
        } catch (Exception error) {
            return ("Erro: " + error);
        }
    }

    @PostMapping("/students/search")
    public String searchStudent(@ModelAttribute("findStudent") Student student, Model model) {
        Student studentEmpty = new Student();
        try {
            Student findStudent = studentService.getStudent(student.getStudentId());
            if (findStudent != studentEmpty) {
                model.addAttribute("student", findStudent);
            } else {
                model.addAttribute("student", studentEmpty);
            }
            return "search_student";
        } catch (Exception e) {
            // return ("Error: " + e);
            return "students";
        }
    }

    @GetMapping("/students/delete/{id}")
    public String deleteStudent(@PathVariable Long id) {
        try {
            studentService.delete(id);
            return "redirect:/students";
        } catch (Exception e) {
            return ("Error: " + e);
        }
    }
}
