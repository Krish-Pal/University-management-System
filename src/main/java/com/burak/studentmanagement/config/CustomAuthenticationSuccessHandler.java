package com.burak.studentmanagement.config;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import com.burak.studentmanagement.entity.Student;
import com.burak.studentmanagement.entity.Teacher;
import com.burak.studentmanagement.service.StudentService;
import com.burak.studentmanagement.service.TeacherService;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	
	@Autowired
	private StudentService studentService;
	
	@Autowired
	private TeacherService teacherService;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication auth) throws IOException, ServletException {
		
		String role = auth.getAuthorities().iterator().next().toString();
		String userName = auth.getName();
		HttpSession session = request.getSession();
		
		// 1. ROLE STUDENT BYPASS
		if(role.equals("ROLE_STUDENT")) {
			if (userName.equals("student")) {
				session.setAttribute("user", new Student());
				response.sendRedirect(request.getContextPath() + "/student/1/courses");
				return;
			}
			
			Student theStudent = studentService.findByStudentName(userName);
			if (theStudent != null) {
				int userId = theStudent.getId();
				session.setAttribute("user", theStudent);
				response.sendRedirect(request.getContextPath() + "/student/" + userId + "/courses");
			} else {
				response.sendRedirect(request.getContextPath() + "/");
			}
			
		// 2. ROLE TEACHER BYPASS
		} else if(role.equals("ROLE_TEACHER")) {
			if (userName.equals("teacher")) {
				session.setAttribute("user", new Teacher());
				response.sendRedirect(request.getContextPath() + "/teacher/1/courses");
				return;
			}
			
			Teacher theTeacher = teacherService.findByTeacherName(userName);
			if (theTeacher != null) {
				int userId = theTeacher.getId();
				session.setAttribute("user", theTeacher);
				response.sendRedirect(request.getContextPath() + "/teacher/" + userId + "/courses");
			} else {
				response.sendRedirect(request.getContextPath() + "/");
			}
			
		// 3. ROLE ADMIN CLEAN PIPELINE
		} else {
			response.sendRedirect(request.getContextPath() + "/admin/adminPanel");
		}
	}
}