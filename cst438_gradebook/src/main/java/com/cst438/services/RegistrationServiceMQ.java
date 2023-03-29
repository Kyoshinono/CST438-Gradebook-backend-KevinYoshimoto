package com.cst438.services;


import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.cst438.domain.Course;
import com.cst438.domain.CourseDTOG;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentDTO;
import com.cst438.domain.EnrollmentRepository;


public class RegistrationServiceMQ extends RegistrationService {

	@Autowired
	EnrollmentRepository enrollmentRepository;

	@Autowired
	CourseRepository courseRepository;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	public RegistrationServiceMQ() {
		System.out.println("MQ registration service ");
	}

	// ----- configuration of message queues

	@Autowired
	Queue registrationQueue;


	// ----- end of configuration of message queue

	// receiver of messages from Registration service
	
	@RabbitListener(queues = "gradebook-queue")
	@Transactional
	public void receive(EnrollmentDTO enrollmentDTO) {
		
		//TODO  complete this method in homework 4
		//create enrollment entity and save to enrollment table in gradebook db
		Enrollment e = new Enrollment();
		e.setStudentName(enrollmentDTO.studentName);
		e.setStudentEmail(enrollmentDTO.studentEmail);
		Course c = courseRepository.findById(enrollmentDTO.course_id).orElse(null);
		if(c==null) {
			System.out.println("Course id not found.");
		}
		e.setCourse(c);
		enrollmentRepository.save(e);
		System.out.println("Added new enrollment, name: " + enrollmentDTO.studentName + ", email: "+ enrollmentDTO.studentEmail + ", course id: " + enrollmentDTO.course_id);
	}

	// sender of messages to Registration Service
	@Override
	public void sendFinalGrades(int course_id, CourseDTOG courseDTO) {
		 
		//TODO  complete this method in homework 4
		//similar to method in registration service, use rabbit template instead of resttemplate
		//rabbit template convert send
		System.out.println("Sending final grade to the registration service for " + course_id);
		rabbitTemplate.convertAndSend(registrationQueue.getName(), courseDTO);
		System.out.println("Message sent to registration service for Grades for course id: " + course_id);
	}

}
