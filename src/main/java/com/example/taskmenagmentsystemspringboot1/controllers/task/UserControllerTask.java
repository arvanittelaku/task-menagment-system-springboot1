package com.example.taskmenagmentsystemspringboot1.controllers.task;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
@RestController
@RequestMapping("/user/tasks")
public class UserControllerTask {



}
