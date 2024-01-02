package com.example.jdbcdemo.queue;

import com.example.jdbcdemo.dtos.MessageDTO;
import com.example.jdbcdemo.queue.dtos.QueueByIdDTO;
import com.example.jdbcdemo.queue.dtos.QueueCreationDTO;
import com.example.jdbcdemo.queue.dtos.QueueDTO;
import com.example.jdbcdemo.utils.ResponseHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api/queues")
@Tag(name = "Queues", description = "Operations related to Queues")
public class QueueController {
    private final QueueService queueService;

    @Autowired
    QueueController(QueueService queueService) {
        this.queueService = queueService;
    }

    @GetMapping()
    @Operation(summary = "Get all queues", description = "Fetches all queues from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of queues",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = QueueDTO.class)))),
            @ApiResponse(responseCode = "500", description = "Error retrieving queues",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class)))
    })
    public ResponseEntity<?> getAllQueues() {
        try {
            ResponseHandler<List<QueueDTO>> responseHandler = queueService.getAllQueues();
            return new ResponseEntity<>(responseHandler.getBody(), responseHandler.getStatusCode());
        } catch (Exception e) {
            System.out.println("Error retrieving queues: " + e.getMessage());
            return new ResponseEntity<>(new MessageDTO("Error retrieving queues"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/owner/{ownerID}")
    @Operation(summary = "Get queues by owner", description = "Retrieves all queues owned by a particular user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of queues",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = QueueDTO.class)))),
            @ApiResponse(responseCode = "404", description = "Cannot find user with this id",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error retrieving queues",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class)))
    })
    public ResponseEntity<?> getQueuesByOwner(@PathVariable("ownerID") int ownerID) {
        try {
            ResponseHandler<List<QueueDTO>> responseHandler = queueService.findQueuesByOwner(ownerID);
            return new ResponseEntity<>(responseHandler.getBody() != null
                    ? responseHandler.getBody()
                    : new MessageDTO(responseHandler.getMessage()), responseHandler.getStatusCode());
        } catch (Exception e) {
            System.out.println("Error retrieving queues: " + e.getMessage());
            return new ResponseEntity<>(new MessageDTO("Error retrieving queues"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("")
    @Operation(summary = "Create queue", description = "Creates a new queue with the given details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Queue successfully created",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = QueueDTO.class))),
            @ApiResponse(responseCode = "404", description = "Cannot find user with this id",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class))),
            @ApiResponse(responseCode = "401", description = "Wrong password",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error creating queue",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class)))
    })
    public ResponseEntity<?> createQueue(@Valid @RequestBody QueueCreationDTO queue, @RequestParam("password") String password) {
        try {
            ResponseHandler<QueueDTO> responseHandler = queueService.addQueue(queue, password);
            return new ResponseEntity<>(responseHandler.getBody() != null
                    ? responseHandler.getBody()
                    : new MessageDTO(responseHandler.getMessage()), responseHandler.getStatusCode());
        } catch (Exception e) {
            System.out.println("Error creating queue: " + e.getMessage());
            return new ResponseEntity<>(new MessageDTO("Error creating queue"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{queueID}")
    @Operation(summary = "Get queue by id", description = "Retrieves detailed information of a specific queue by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Detailed information about the queue has been received successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = QueueByIdDTO.class))),
            @ApiResponse(responseCode = "404", description = "Cannot find queue with this id",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error finding queue",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class)))
    })
    public ResponseEntity<?> getQueueById(@PathVariable("queueID") int queueID) {
        try {
            ResponseHandler<QueueByIdDTO> responseHandler = queueService.findQueueById(queueID);
            return new ResponseEntity<>(responseHandler.getBody() != null
                    ? responseHandler.getBody()
                    : new MessageDTO(responseHandler.getMessage()), responseHandler.getStatusCode());
        } catch (Exception e) {
            System.out.println("Error finding queue: " + e.getMessage());
            return new ResponseEntity<>(new MessageDTO("Error finding queue"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/name/{name}")
    @Operation(summary = "Get queues by name", description = "Retrieves all queues that match a given name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of queues",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = QueueDTO.class)))),
            @ApiResponse(responseCode = "500", description = "Error finding queues by name",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class)))
    })
    public ResponseEntity<?> getQueuesByName(@PathVariable("name") String name) {
        try {
            ResponseHandler<List<QueueDTO>> responseHandler = queueService.findQueuesByName(name);
            return new ResponseEntity<>(responseHandler.getBody(), responseHandler.getStatusCode());
        } catch (Exception e) {
            System.out.println("Error finding queues by name: " + e.getMessage());
            return new ResponseEntity<>(new MessageDTO("Error finding queues by name"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{queueID}")
    @Operation(summary = "Delete queue", description = "Removes a specific queue from the system by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Queue was deleted successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class))),
            @ApiResponse(responseCode = "404", description = "Cannot find queue with this id",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class))),
            @ApiResponse(responseCode = "401", description = "Wrong queue code",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error deleting queue",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class)))
    })
    public ResponseEntity<?> deleteQueue(@PathVariable("queueID") int queueID, @RequestParam("code") String code) {
        try {
            ResponseHandler<?> responseHandler = queueService.deleteQueueById(queueID, code);
            return new ResponseEntity<>(new MessageDTO(responseHandler.getMessage()), responseHandler.getStatusCode());
        } catch (Exception e) {
            System.out.println("Error deleting queue: " + e.getMessage());
            return new ResponseEntity<>(new MessageDTO("Error deleting queue"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/close/{queueID}")
    @Operation(summary = "Close queue", description = "Closes the queue, preventing new users from joining")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - various results",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "Queue is closed", value = "{'message': 'Queue is closed'}"),
                            @ExampleObject(name = "Queue is closed and deleted", value = "{'message': 'Queue is closed and deleted'}")
                    }, schema = @Schema(implementation = MessageDTO.class))),
            @ApiResponse(responseCode = "404", description = "Cannot find queue with this id",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class))),
            @ApiResponse(responseCode = "401", description = "Wrong queue code",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error closing queue",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class)))
    })
    public ResponseEntity<?> closeQueue(@PathVariable("queueID") int queueID, @RequestParam(name = "code") String code) {
        try {
            ResponseHandler<?> responseHandler = queueService.closeQueue(queueID, code);
            return new ResponseEntity<>(new MessageDTO(responseHandler.getMessage()), responseHandler.getStatusCode());
        } catch (Exception e) {
            System.out.println("Error closing queue: " + e.getMessage());
            return new ResponseEntity<>(new MessageDTO("Error closing queue"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/open/{queueID}")
    @Operation(summary = "Open queue", description = "Opens the queue, allowing users to join")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Queue is opened",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class))),
            @ApiResponse(responseCode = "404", description = "Cannot find queue with this id",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class))),
            @ApiResponse(responseCode = "401", description = "Wrong queue code",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error opening queue",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class)))
    })
    public ResponseEntity<?> openQueue(@PathVariable("queueID") int queueID, @RequestParam(name = "code") String code) {
        try {
            ResponseHandler<?> responseHandler = queueService.openQueue(queueID, code);
            return new ResponseEntity<>(new MessageDTO(responseHandler.getMessage()), responseHandler.getStatusCode());
        } catch (Exception e) {
            System.out.println("Error opening queue: " + e.getMessage());
            return new ResponseEntity<>(new MessageDTO("Error opening queue"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
