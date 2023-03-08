package com.cst438.controllers;

@RestController
public class AssignmentController {

	@PostMapping("/assignment")
	public String createNewAssignment() {
		return "assignment id = 45678";
	}
}
