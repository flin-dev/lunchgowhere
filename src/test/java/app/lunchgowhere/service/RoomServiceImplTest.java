package app.lunchgowhere.service;

import app.lunchgowhere.dto.request.LocationSubmissionDto;
import app.lunchgowhere.dto.request.RoomDto;
import app.lunchgowhere.model.LocationSubmission;
import app.lunchgowhere.model.Room;
import app.lunchgowhere.model.User;
import app.lunchgowhere.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class RoomServiceImplTest {

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private RoomServiceImpl service;

    @Mock
    private UserService userService;

    @Mock
    private User mockUser;

    @BeforeEach
    public void setUp() {
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("Test User");
    }

    @Test
    public void testGetRoom() {
        // Given
        Long roomId = 1L;
        Room mockRoom = new Room();
        mockRoom.setId(roomId);
        when(roomRepository.findById(roomId)).thenReturn(Optional.of(mockRoom));

        // When
        Room result = service.getRoom(roomId);

        // Then
        assertNotNull(result);
        assertEquals(roomId, result.getId());
        verify(roomRepository).findById(roomId);
    }

    @Test
    public void testGetRoomNotFound() {
        // Given
        Long roomId = 1L;
        when(roomRepository.findById(roomId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(NoSuchElementException.class, () -> {
            service.getRoom(roomId);
        });
    }

    @Test
    public void testCreateRoom() {
        // Given
        Room mockRoom = new Room();
        mockRoom.setId(1L);
        mockRoom.setName("Test Room");
        mockRoom.setRoomOwner(mockUser);

        RoomDto roomDto = new RoomDto();
        roomDto.setName("Test Room");
        roomDto.setDescription("Test Description");
        roomDto.setTargetTime("2021-08-01T12:00:00");

        when(roomRepository.save(any(Room.class))).thenReturn(mockRoom);

        when(userService.getUserByUsername(mockUser.getUsername())).thenReturn(mockUser);

        // When
        Room result = service.createRoom(roomDto, mockUser.getUsername());

        // Then
        assertNotNull(result);
        assertEquals(mockRoom.getId(), result.getId());
        assertEquals(mockRoom.getName(), result.getName());
        assertEquals(mockRoom.getRoomOwner(), result.getRoomOwner());
        verify(roomRepository).save(any(Room.class));
    }

    @Test
    public void testCloseRoomIfOwner() {
        // Given
        Long roomId = 1L;
        Room mockRoom = new Room();
        mockRoom.setId(roomId);
        mockRoom.setName("Test Room");
        mockRoom.setRoomOwner(mockUser);
        mockRoom.setIsActive(true);
        when(roomRepository.findById(roomId)).thenReturn(Optional.of(mockRoom));
        when(roomRepository.save(any(Room.class))).thenReturn(mockRoom);

        // When
        Boolean result = service.closeRoom(roomId.toString(), "Test User");

        // Then
        assertTrue(result);
        assertFalse(mockRoom.getIsActive());
        verify(roomRepository).findById(roomId);
        verify(roomRepository).save(any(Room.class));
    }

    @Test
    public void testCloseRoomNotFound() {
        // Given
        Long roomId = 1L;
        when(roomRepository.findById(roomId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NoSuchElementException.class, () -> {
            service.closeRoom(roomId.toString(), "Test User");
        });

        verify(roomRepository).findById(roomId);
        verify(roomRepository, never()).save(any(Room.class));
    }

    @Test
    public void testCloseRoomNotOwner() {
        // Given
        Long roomId = 1L;
        Room mockRoom = new Room();
        mockRoom.setId(roomId);
        mockRoom.setName("Test Room");
        mockRoom.setRoomOwner(mockUser);
        mockRoom.setIsActive(true);
        when(roomRepository.findById(roomId)).thenReturn(Optional.of(mockRoom));

        User mockUser2 = new User();
        mockUser2.setId(2L);
        mockUser2.setUsername("Not Owner");

        // When
        Boolean result = service.closeRoom(roomId.toString(), mockUser2.getUsername());

        // Then
        assertFalse(result);
        assertTrue(mockRoom.getIsActive());
        verify(roomRepository).findById(roomId);
        verify(roomRepository, never()).save(any(Room.class));
    }



    //not yet finish feature
    @Test
    public void testCloseAndPickLocation() {
        // Given
//        Long roomId = 1L;
//
//        LocationSubmission submission1 = new LocationSubmission();
//        LocationSubmission submission2 = new LocationSubmission();
//        // Set properties for submissions as required
//
//        List<LocationSubmission> submissions = new ArrayList<>();
//        submissions.add(submission1);
//        submissions.add(submission2);
//
//        Room mockRoom = new Room();
//        mockRoom.setId(roomId);
//        mockRoom.setName("Test Room");
//        mockRoom.setRoomOwner(mockUser);
//        mockRoom.setIsActive(true);
//        mockRoom.setLocationSubmissions(submissions);
//
//        when(roomRepository.findById(roomId)).thenReturn(Optional.of(mockRoom));
//        when(service.closeRoom(roomId.toString(), mockUser.getUsername())).thenReturn(true);
//        when(roomRepository.findById(roomId)).thenReturn(Optional.of(mockRoom));
////        when(roomRepository.save(null)).thenReturn(mockRoom);
//        when(roomRepository.save(any(Room.class))).thenReturn(mockRoom);
//
//        when(userService.getUserByUsername(mockUser.getUsername())).thenReturn(mockUser);
//
//
//
//        // When
//        Optional<LocationSubmission> result = service.closeAndPickLocation(roomId.toString(), mockUser.getUsername());
//
//        // Then
//        assertTrue(result.isPresent());
//        assertTrue(result.get().getSelected());
//        verify(roomRepository, times(2)).findById(roomId); // Expecting findById to be called twice
//        verify(roomRepository).save(any(Room.class));
    }


    //not yet finish feature
    @Test
    public void testCloseAndPickLocationNotFound() {
//        // Given
//        Long roomId = 1L;
//        when(roomRepository.findById(roomId)).thenReturn(Optional.empty());
//
//        // When
//        Optional<LocationSubmission> result = service.closeAndPickLocation(roomId.toString(), "Test Owner");
//
//        // Then
//        assertFalse(result.isPresent());
//        verify(roomRepository).findById(roomId);
//        verify(roomRepository, never()).save(any(Room.class));
    }

    //not yet finish feature
//    @Test
//    public void testCloseAndPickLocationNotOwner() {
//        // Given
//        Long roomId = 1L;
//        Room mockRoom = new Room();
//        mockRoom.setId(roomId);
//        mockRoom.setName("Test Room");
//        mockRoom.setRoomOwner(mockUser);
//        mockRoom.setIsActive(true);
//        when(roomRepository.findById(roomId)).thenReturn(Optional.of(mockRoom));
//
//        User mockUser2 = new User();
//        mockUser2.setId(2L);
//        mockUser2.setUsername("Not Owner");
//
//        // When
//        Optional<LocationSubmission> result = service.closeAndPickLocation(roomId.toString(), mockUser2.getUsername());
//
//        // Then
//        assertFalse(result.isPresent());
//        verify(roomRepository).findById(roomId);
//        verify(roomRepository, never()).save(any(Room.class));
//    }

    @Test
    public void testCreateLocationSubmission() {
        Long roomId = 1L;
        Room mockRoom = new Room();
        mockRoom.setId(roomId);
        mockRoom.setName("Test Room");
        mockRoom.setRoomOwner(mockUser);
        mockRoom.setIsActive(true);
        // Given
//        LocationSubmissionDto mockLocationSubmission = new LocationSubmissionDto();
//        mockLocationSubmission.setName("Test Location Submission");
//        mockLocationSubmission.setReason("Test Reason");
//        mockLocationSubmission.setRoomId(roomId);
//        mockLocationSubmission.htmlEscape();
//
//        when(roomRepository.save(any(Room.class))).thenReturn(mockRoom);

        // When
//        LocationSubmission result = service.createLocationSubmission(mockLocationSubmission, "Test Owner");
//
//        // Then
//        assertNotNull(result);
//        assertEquals(mockLocationSubmission.getId(), result.getId());
//        assertEquals(mockLocationSubmission.getName(), result.getName());
//        assertEquals(mockLocationSubmission.getOwner(), result.getOwner());
//        verify(roomRepository).save(any(Room.class));
    }

}