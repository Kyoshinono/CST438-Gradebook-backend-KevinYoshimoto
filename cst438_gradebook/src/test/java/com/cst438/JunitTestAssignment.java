package com.cst438;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.cst438.controllers.AssignmentController;
import com.cst438.domain.Assignment;
import com.cst438.domain.AssignmentGrade;
import com.cst438.domain.AssignmentGradeRepository;
import com.cst438.domain.AssignmentRepository;
import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.Enrollment;
import com.cst438.services.RegistrationService;
import com.fasterxml.jackson.databind.ObjectMapper;
 

import org.springframework.test.context.ContextConfiguration;


@ContextConfiguration(classes = {AssignmentController.class})
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest
public class JunitTestAssignment {

	static final String URL = "http://localhost:8080";
	public static final int TEST_COURSE_ID = 40442;
	public static final String TEST_STUDENT_EMAIL = "test@csumb.edu";
	public static final String TEST_STUDENT_NAME = "test";
	public static final String TEST_INSTRUCTOR_EMAIL = "dwisneski@csumb.edu";
	public static final int TEST_YEAR = 2021;
	public static final String TEST_SEMESTER = "Fall";

	@MockBean
	AssignmentRepository assignmentRepository;

	@MockBean
	AssignmentGradeRepository assignmentGradeRepository;

	@MockBean
	CourseRepository courseRepository; // must have this to keep Spring test happy

	@MockBean
	RegistrationService registrationService; // must have this to keep Spring test happy

	@Autowired
	private MockMvc mvc;
	
	@Test
	public void deleteAssignment() throws Exception {

		MockHttpServletResponse response;

		// mock database data

		Course course = new Course();
		course.setCourse_id(TEST_COURSE_ID);
		course.setSemester(TEST_SEMESTER);
		course.setYear(TEST_YEAR);
		course.setInstructor(TEST_INSTRUCTOR_EMAIL);
		course.setEnrollments(new java.util.ArrayList<Enrollment>());
		course.setAssignments(new java.util.ArrayList<Assignment>());

		Enrollment enrollment = new Enrollment();
		enrollment.setCourse(course);
		course.getEnrollments().add(enrollment);
		enrollment.setId(TEST_COURSE_ID);
		enrollment.setStudentEmail(TEST_STUDENT_EMAIL);
		enrollment.setStudentName(TEST_STUDENT_NAME);

		Assignment assignment = new Assignment();
		assignment.setCourse(course);
		course.getAssignments().add(assignment);
		// set dueDate to 1 week before now.
		assignment.setDueDate(new java.sql.Date(System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000));
		assignment.setId(1);
		assignment.setName("Assignment 1");
		assignment.setNeedsGrading(1);
		
		Assignment assignment2 = new Assignment();
		assignment2.setCourse(course);
		course.getAssignments().add(assignment2);
		// set dueDate to 1 week before now.
		assignment2.setDueDate(new java.sql.Date(System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000));
		assignment2.setId(2);
		assignment2.setName("Assignment 2");
		assignment2.setNeedsGrading(1);

		AssignmentGrade ag = new AssignmentGrade();
		ag.setAssignment(assignment);
		ag.setId(1);
		ag.setScore("80");
		ag.setStudentEnrollment(enrollment);
		List<AssignmentGrade> agL = new ArrayList<>();
		agL.add(ag);
		
		AssignmentGrade ag2 = new AssignmentGrade();
		ag2.setAssignment(assignment2);
		ag2.setId(2);
		List<AssignmentGrade> agL2 = new ArrayList<>();
		agL2.add(ag2);

		// given -- stubs for database repositories that return test data
		given(assignmentRepository.findById(1)).willReturn(Optional.of(assignment));
		given(assignmentGradeRepository.findByAssignmentIdAndStudentEmail(1, TEST_STUDENT_EMAIL)).willReturn(ag);
		given(assignmentGradeRepository.findById(1)).willReturn(Optional.of(ag));
		given(courseRepository.findById(TEST_COURSE_ID)).willReturn(Optional.of(course));
		given(assignmentGradeRepository.findById(2)).willReturn(Optional.of(ag2));
		given(assignmentRepository.findById(2)).willReturn(Optional.of(assignment2));
		given(assignmentGradeRepository.findByAssignmentId(1)).willReturn(agL);
		given(assignmentGradeRepository.findByAssignmentId(2)).willReturn(agL2);

		// end of mock data
		//assignmentRepository.save(assignment);
		
		response = mvc.perform(MockMvcRequestBuilders.delete("/assignment/1").accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		
		// verify that return status = Bad_request (value 400)
		assertEquals(400, response.getStatus());
		
		//TODO verify the assignment was deleted.
		//verify(assignmentRepository).delete(any(Assignment.class));
		
		response = mvc.perform(MockMvcRequestBuilders.delete("/assignment/2").accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		
		// verify that return status = OK (value 200)
		assertEquals(200, response.getStatus());
		

	}
	
	@Test
	public void changeAssignmentName() throws Exception {

		MockHttpServletResponse response;

		// mock database data

		Course course = new Course();
		course.setCourse_id(TEST_COURSE_ID);
		course.setSemester(TEST_SEMESTER);
		course.setYear(TEST_YEAR);
		course.setInstructor(TEST_INSTRUCTOR_EMAIL);
		course.setEnrollments(new java.util.ArrayList<Enrollment>());
		course.setAssignments(new java.util.ArrayList<Assignment>());

		Enrollment enrollment = new Enrollment();
		enrollment.setCourse(course);
		course.getEnrollments().add(enrollment);
		enrollment.setId(TEST_COURSE_ID);
		enrollment.setStudentEmail(TEST_STUDENT_EMAIL);
		enrollment.setStudentName(TEST_STUDENT_NAME);

		Assignment assignment = new Assignment();
		assignment.setCourse(course);
		course.getAssignments().add(assignment);
		// set dueDate to 1 week before now.
		assignment.setDueDate(new java.sql.Date(System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000));
		assignment.setId(1);
		assignment.setName("Assignment 1");
		assignment.setNeedsGrading(1);

		AssignmentGrade ag = new AssignmentGrade();
		ag.setAssignment(assignment);
		ag.setId(1);
		ag.setScore("80");
		ag.setStudentEnrollment(enrollment);

		// given -- stubs for database repositories that return test data
		given(assignmentRepository.findById(1)).willReturn(Optional.of(assignment));
		given(assignmentGradeRepository.findByAssignmentIdAndStudentEmail(1, TEST_STUDENT_EMAIL)).willReturn(ag);
		given(assignmentGradeRepository.findById(1)).willReturn(Optional.of(ag));
		given(courseRepository.findById(TEST_COURSE_ID)).willReturn(Optional.of(course));

		// end of mock data
		
		Assignment result = new Assignment();
		result.setName("NewTest");
		
		// send updates to server
		response = mvc
				.perform(MockMvcRequestBuilders.patch("/assignment/1").accept(MediaType.APPLICATION_JSON)
						.content(asJsonString(result)).contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		
		// verify that return status = OK (value 200)
		assertEquals(200, response.getStatus());
		
		//TODO verify that the name has been changed
		
		
	}
	
	@Test
	public void createAssignment() throws Exception {
		
		MockHttpServletResponse response;

		// mock database data

		Course course = new Course();
		course.setCourse_id(TEST_COURSE_ID);
		course.setSemester(TEST_SEMESTER);
		course.setYear(TEST_YEAR);
		course.setInstructor(TEST_INSTRUCTOR_EMAIL);
		course.setEnrollments(new java.util.ArrayList<Enrollment>());
		course.setAssignments(new java.util.ArrayList<Assignment>());

		Enrollment enrollment = new Enrollment();
		enrollment.setCourse(course);
		course.getEnrollments().add(enrollment);
		enrollment.setId(TEST_COURSE_ID);
		enrollment.setStudentEmail(TEST_STUDENT_EMAIL);
		enrollment.setStudentName(TEST_STUDENT_NAME);

		Assignment assignment = new Assignment();
		assignment.setCourse(course);
		course.getAssignments().add(assignment);
		// set dueDate to 1 week before now.
		assignment.setDueDate(new java.sql.Date(System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000));
		assignment.setId(1);
		assignment.setName("Assignment 1");
		assignment.setNeedsGrading(1);

		AssignmentGrade ag = new AssignmentGrade();
		ag.setAssignment(assignment);
		ag.setId(1);
		ag.setScore("80");
		ag.setStudentEnrollment(enrollment);

		// given -- stubs for database repositories that return test data
		given(assignmentRepository.findById(1)).willReturn(Optional.of(assignment));
		given(assignmentGradeRepository.findByAssignmentIdAndStudentEmail(1, TEST_STUDENT_EMAIL)).willReturn(ag);
		given(assignmentGradeRepository.findById(1)).willReturn(Optional.of(ag));
		given(courseRepository.findById(TEST_COURSE_ID)).willReturn(Optional.of(course));

		// end of mock data
		
		Assignment result = new Assignment();
		result.setDueDate(assignment.getDueDate());
		result.setName("MockTestAssignment");	
		//Transform the mock data into json format
		given(assignmentRepository.save(any(Assignment.class))).willReturn(result);
		
		// then do an http post request for course 40442
		response = mvc.perform(MockMvcRequestBuilders.post("/course/40442").accept(MediaType.APPLICATION_JSON)
				.content(asJsonString(result)).contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();
		
		// verify that return status = OK (value 200)
		assertEquals(200, response.getStatus());
		
		//Verify the repository save method was called
		assertThat(result.getId()).isNotNull();
		verify(assignmentRepository).save(any(Assignment.class));
	}

	private static String asJsonString(final Object obj) {
		try {

			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static <T> T fromJsonString(String str, Class<T> valueType) {
		try {
			return new ObjectMapper().readValue(str, valueType);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
