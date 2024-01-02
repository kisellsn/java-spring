package com.example.jdbcdemo.relation;

import com.example.jdbcdemo.dtos.MessageDTO;
import com.example.jdbcdemo.user.dtos.PlaceInQueueDTO;
import com.example.jdbcdemo.utils.ResponseHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api/relations")
@Tag(name = "Relations", description = "Operations related to Relations")
public class RelationController {
    private final RelationService relationService;

    @Autowired
    RelationController(RelationService relationshipService) {
        this.relationService = relationshipService;
    }

    @PostMapping("/{userID}/{queueID}")
    @Operation(summary = "Join to queue", description = "Allows a user to join a specific queue")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully joined the queue",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PlaceInQueueDTO.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND - various reasons",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "User does not exist", value = "{'message': 'User does not exist'}"),
                            @ExampleObject(name = "Queue does not exist", value = "{'message': 'Queue does not exist'}")
                    }, schema = @Schema(implementation = MessageDTO.class))),
            @ApiResponse(responseCode = "409", description = "CONFLICT - various reasons",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "Queue is locked", value = "{'message': 'Queue is locked'}"),
                            @ExampleObject(name = "User is already in the queue", value = "{'message': 'User is already in the queue'}")
                    }, schema = @Schema(implementation = MessageDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error joining queue",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class)))
    })
    public ResponseEntity<?> joinUserQueue(@PathVariable("userID") int userID, @PathVariable("queueID") int queueID) {
        try {
            ResponseHandler<PlaceInQueueDTO> responseHandler = relationService.joinUserQueue(userID, queueID);
            return new ResponseEntity<>(responseHandler.getBody() != null
                    ? responseHandler.getBody()
                    : new MessageDTO(responseHandler.getMessage()), responseHandler.getStatusCode());
        } catch (Exception e) {
            System.out.println("Error joining queue: " + e.getMessage());
            return new ResponseEntity<>(new MessageDTO("Error joining queue"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{userID}/{queueID}")
    @Operation(summary = "Get place", description = "Retrieves the user's position in a specific queue")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Place of the user in the queue successfully retrieved",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PlaceInQueueDTO.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND - various reasons",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "User does not exist", value = "{'message': 'User does not exist'}"),
                            @ExampleObject(name = "Queue does not exist", value = "{'message': 'Queue does not exist'}"),
                            @ExampleObject(name = "The user is not in the queue", value = "{'message': 'The user is not in the queue'}")
                    }, schema = @Schema(implementation = MessageDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error getting place",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class)))
    })
    public ResponseEntity<?> getPlace(@PathVariable("userID") int userID, @PathVariable("queueID") int queueID) {
        try {
            ResponseHandler<PlaceInQueueDTO> responseHandler = relationService.getPlace(userID, queueID);
            return new ResponseEntity<>(responseHandler.getBody() != null
                    ? responseHandler.getBody()
                    : new MessageDTO(responseHandler.getMessage()), responseHandler.getStatusCode());
        } catch (Exception e) {
            System.out.println("Error getting place: " + e.getMessage());
            return new ResponseEntity<>(new MessageDTO("Error getting place"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{queueID}")
    @Operation(summary = "Delete head of the queue", description = "Removes the first user in a specific queue")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - various results",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "Head of the queue removed successfully", value = "{'message': 'Head of the queue removed successfully'}"),
                            @ExampleObject(name = "Last user removed from queue successfully and queue is deleted", value = "{'message': 'Last user removed from queue successfully and queue is deleted'}")
                    }, schema = @Schema(implementation = MessageDTO.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND - various reasons",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "Queue does not exist", value = "{'message': 'Queue does not exist'}"),
                            @ExampleObject(name = "The user is not in the queue", value = "{'message': 'The user is not in the queue'}")
                    }, schema = @Schema(implementation = MessageDTO.class))),
            @ApiResponse(responseCode = "409", description = "Queue is empty",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class))),
            @ApiResponse(responseCode = "401", description = "Wrong queue code",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error removing head of the queue",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class)))
    })
    public ResponseEntity<?> deleteHeadOfQueue(@PathVariable("queueID") int queueID, @RequestParam("code") String code) {
        try {
            ResponseHandler<?> responseHandler = relationService.deleteHeadOfQueue(queueID, code);
            return new ResponseEntity<>(new MessageDTO(responseHandler.getMessage()), responseHandler.getStatusCode());
        } catch (Exception e) {
            System.out.println("Error removing head of the queue: " + e.getMessage());
            return new ResponseEntity<>(new MessageDTO("Error removing head of the queue"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{queueID}/{userID}")
    @Operation(summary = "Remove user from queue by owner", description = "Allows the owner of a queue to remove a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - various results",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "User removed from queue successfully", value = "{'message': 'User removed from queue successfully'}"),
                            @ExampleObject(name = "Last user removed from queue successfully and queue is deleted", value = "{'message': 'Last user removed from queue successfully and queue is deleted'}")
                    }, schema = @Schema(implementation = MessageDTO.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND - various reasons",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "User does not exist", value = "{'message': 'User does not exist'}"),
                            @ExampleObject(name = "Queue does not exist", value = "{'message': 'Queue does not exist'}"),
                            @ExampleObject(name = "The user is not in the queue", value = "{'message': 'The user is not in the queue'}")
                    }, schema = @Schema(implementation = MessageDTO.class))),
            @ApiResponse(responseCode = "401", description = "Wrong queue code",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error removing user from the queue",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class)))
    })
    public ResponseEntity<?> removeUserByOwner(
            @PathVariable("userID") int userID,
            @PathVariable("queueID") int queueID,
            @RequestParam("code") String code
    ) {
        try {
            ResponseHandler<?> responseHandler = relationService.removeUser(userID, queueID, code);
            return new ResponseEntity<>(new MessageDTO(responseHandler.getMessage()), responseHandler.getStatusCode());
        } catch (Exception e) {
            System.out.println("Error removing user from the queue: " + e.getMessage());
            return new ResponseEntity<>(new MessageDTO("Error removing user from the queue"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/exit/{userID}/{queueID}")
    @Operation(summary = "User exit", description = "Allows a user to remove themselves from a specific queue")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - various results",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "User exited from queue successfully", value = "{'message': 'User exited from queue successfully'}"),
                            @ExampleObject(name = "Last user exited from queue successfully and queue is deleted", value = "{'message': 'Last user exited from queue successfully and queue is deleted'}")
                    },schema = @Schema(implementation = MessageDTO.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND - various reasons",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "User does not exist", value = "{'message': 'User does not exist'}"),
                            @ExampleObject(name = "Queue does not exist", value = "{'message': 'Queue does not exist'}"),
                            @ExampleObject(name = "The user is not in the queue", value = "{'message': 'The user is not in the queue'}")
                    }, schema = @Schema(implementation = MessageDTO.class))),
            @ApiResponse(responseCode = "401", description = "Wrong password",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error removing user from the queue",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class)))
    })
    public ResponseEntity<?> userExit(
            @PathVariable("userID") int userID,
            @PathVariable("queueID") int queueID,
            @RequestParam("password") String password
    ) {
        try {
            ResponseHandler<?> responseHandler = relationService.exitFromQueue(userID, queueID, password);
            return new ResponseEntity<>(new MessageDTO(responseHandler.getMessage()), responseHandler.getStatusCode());
        } catch (Exception e) {
            System.out.println("Error removing user from the queue: " + e.getMessage());
            return new ResponseEntity<>(new MessageDTO("Error removing user from the queue"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
