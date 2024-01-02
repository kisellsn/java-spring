package com.example.jdbcdemo.user;

import com.example.jdbcdemo.dtos.MessageDTO;
import com.example.jdbcdemo.user.dtos.UserDTO;
import com.example.jdbcdemo.user.dtos.UserIdDTO;
import com.example.jdbcdemo.user.dtos.UserRegisterDTO;
import com.example.jdbcdemo.user.dtos.UserUpdateDTO;
import com.example.jdbcdemo.utils.ResponseHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Operations related to Users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    @Operation(summary = "Get all users", description = "Fetches all users from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of users",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UserDTO.class)))),
            @ApiResponse(responseCode = "500", description = "Error retrieving users",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class)))
    })

    public ResponseEntity<?> getAllUsers() {
        try {
            ResponseHandler<List<UserDTO>> responseHandler = userService.getAllUsers();
            return new ResponseEntity<>(responseHandler.getBody(), responseHandler.getStatusCode());
        } catch (Exception e) {
            System.out.println("Error retrieving users" + e.getMessage());
            return new ResponseEntity<>(new MessageDTO("Error retrieving users"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/owners")
    @Operation(summary = "Get all users who own queues", description = "Fetches all users who own one or more queues")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of users",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UserDTO.class)))),
            @ApiResponse(responseCode = "500", description = "Error retrieving users",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class)))
    })
    public ResponseEntity<?> getAllOwners() {
        try {
            ResponseHandler<List<UserDTO>> responseHandler = userService.getAllOwners();
            return new ResponseEntity<>(responseHandler.getBody(), responseHandler.getStatusCode());
        } catch (Exception e) {
            System.out.println("Error retrieving users: " + e.getMessage());
            return new ResponseEntity<>(new MessageDTO("Error retrieving users"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/queue/{queueID}")
    @Operation(summary = "Get all users from queue", description = "Retrieves all users associated with a particular queue")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of users",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UserDTO.class)))),
            @ApiResponse(responseCode = "404", description = "Cannot find queue with this id",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error retrieving users",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class)))
    })
    public ResponseEntity<?> getUsersByQueue(@PathVariable("queueID") int queueID) {
        try {
            ResponseHandler<List<UserDTO>> responseHandler = userService.getUsersByQueue(queueID);
            return new ResponseEntity<>(responseHandler.getBody() != null
                    ? responseHandler.getBody()
                    : new MessageDTO(responseHandler.getMessage()), responseHandler.getStatusCode());
        } catch (Exception e) {
            System.out.println("Error retrieving users: " + e.getMessage());
            return new ResponseEntity<>(new MessageDTO("Error retrieving users"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping()
    @Operation(summary = "Create user", description = "Registers a new user with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User successfully created",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "409", description = "There is a user with this login",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error creating user",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class)))
    })
    public ResponseEntity<?> createUser(@Valid @RequestBody UserRegisterDTO user) {
        try {
            ResponseHandler<UserDTO> responseHandler = userService.addUser(user);
            return new ResponseEntity<>(responseHandler.getBody() != null
                    ? responseHandler.getBody()
                    : new MessageDTO(responseHandler.getMessage()), responseHandler.getStatusCode());
        } catch (Exception e) {
            System.out.println("Error creating user: " + e.getMessage());
            return new ResponseEntity<>(new MessageDTO("Error creating user"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{userID}")
    @Operation(summary = "Get user by id", description = "Retrieves detailed information of a specific user by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Detailed information about the user has been received successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserIdDTO.class))),
            @ApiResponse(responseCode = "404", description = "Cannot find users with this id",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error finding user",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class)))
    })
    public ResponseEntity<?> getUserById(@PathVariable("userID") int userID) {
        try {
            ResponseHandler<UserIdDTO> responseHandler =  userService.findUserById(userID);
            return new ResponseEntity<>(responseHandler.getBody() != null
                    ? responseHandler.getBody()
                    : new MessageDTO(responseHandler.getMessage()), responseHandler.getStatusCode());
        } catch (Exception e) {
            System.out.println("Error finding user: " + e.getMessage());
            return new ResponseEntity<>(new MessageDTO("Error finding user"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/name/{login}")
    @Operation(summary = "Get user by name", description = "Retrieves user information based on their login name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Information about the user has been received successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "404", description = "User is not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error finding user by login",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class)))
    })
    public ResponseEntity<?> getUserByName(@PathVariable("login") String login) {
        try {
            ResponseHandler<UserDTO> responseHandler =  userService.findUserByLogin(login);
            return new ResponseEntity<>(responseHandler.getBody() != null
                    ? responseHandler.getBody()
                    : new MessageDTO(responseHandler.getMessage()), responseHandler.getStatusCode());
        } catch (Exception e) {
            System.out.println("Error finding user by login: " + e.getMessage());
            return new ResponseEntity<>(new MessageDTO("Error finding user by login"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("")
    @Operation(summary = "Change username", description = "Updates the login name for a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "New user login successfully added",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "404", description = "Cannot find User with this id",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class))),
            @ApiResponse(responseCode = "409", description = "There is a user with this login",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class))),
            @ApiResponse(responseCode = "401", description = "Wrong password",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error updating user",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class)))
    })
    public ResponseEntity<?> updateUser(@Valid @RequestBody UserUpdateDTO newUser, @RequestParam("password") String password) {
        try {
            ResponseHandler<UserDTO> responseHandler =  userService.updateUser(newUser, password);
            return new ResponseEntity<>(responseHandler.getBody() != null
                    ? responseHandler.getBody()
                    : new MessageDTO(responseHandler.getMessage()), responseHandler.getStatusCode());
        } catch (Exception e) {
            System.out.println("Error updating user: " + e.getMessage());
            return new ResponseEntity<>(new MessageDTO("Error updating user"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{userID}")
    @Operation(summary = "Delete user", description = "Removes a specific user from the system by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User was deleted successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class))),
            @ApiResponse(responseCode = "404", description = "Cannot find user with this id",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class))),
            @ApiResponse(responseCode = "401", description = "Wrong password",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error deleting user",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class)))
    })
    public ResponseEntity<?> deleteUser(@PathVariable("userID") int userID, @RequestParam("password") String password) {
        try {
            ResponseHandler<?> responseHandler =  userService.deleteUserById(userID, password);
            return new ResponseEntity<>(new MessageDTO(responseHandler.getMessage()), responseHandler.getStatusCode());
        } catch (Exception e) {
            System.out.println("Error deleting user: " + e.getMessage());
            return new ResponseEntity<>(new MessageDTO("Error deleting user"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
