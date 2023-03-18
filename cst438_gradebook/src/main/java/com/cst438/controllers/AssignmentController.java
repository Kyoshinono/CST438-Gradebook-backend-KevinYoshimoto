package com.cst438.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cst438.domain.Assignment;
import com.cst438.domain.AssignmentGrade;
import com.cst438.domain.AssignmentGradeRepository;
import com.cst438.domain.AssignmentRepository;
import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;
import com.cst438.services.RegistrationService;

@RestController
@CrossOrigin(origins = {"http://localhost:3000","http://localhost:3001"})
public class AssignmentController {
	
	
	@Autowired
	AssignmentRepository assignmentRepository;
	
	@Autowired
	AssignmentGradeRepository assignmentGradeRepository;
	
	@Autowired
	CourseRepository courseRepository;
	
	@Autowired
	RegistrationService registrationService;
	
	//Posting a new assignment giving the name and the dueDate in the body and the course id in the path
	@PostMapping("/course/{id}")
	public void createNewAssignment(@RequestBody Assignment assign, @PathVariable("id") int course_id) {
		String email = "dwisneski@csumb.edu";  // user name (should be instructor's email)
		
		Course c = courseRepository.findById(course_id).orElse(null);
		if (!c.getInstructor().equals(email)) {
			throw new ResponseStatusException( HttpStatus.UNAUTHORIZED, "Not Authorized." );
		}

		if(assign.getDueDate() == null) {
			throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "Invalid Due Date.");
		}
		
		if(assign.getName() == null) {
			throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "Invalid Name.");
		}
		
		assign.setCourse(c);
		
		assignmentRepository.save(assign);

	}
	
	//Put request to change the name of an assignment
	@PatchMapping("/assignment/{id}")
	public void changeAssignmentName(@RequestBody Assignment assign, @PathVariable("id") int id) {

		Assignment ag = assignmentRepository.findById(id).orElse(null);
		ag.setName(assign.getName());
		
		assignmentRepository.save(ag);
	}
	
	//Delete request to delete the assignment from the db
	@DeleteMapping("/assignment/{id}")
	public void deleteAssignment(@PathVariable("id") int id) {

		Assignment ag = assignmentRepository.findById(id).orElse(null);
		List<AssignmentGrade> asg = assignmentGradeRepository.findByAssignmentId(ag.getId());
		
		for(int i = 0; i < asg.size(); i++) {
			String score = asg.get(i).getScore();
			if(score != null){
				throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "There are Grades for the Assignment");
			}
		}
		
		
		assignmentRepository.delete(ag);
	}
}
