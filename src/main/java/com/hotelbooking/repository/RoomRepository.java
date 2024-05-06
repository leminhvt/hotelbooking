package com.hotelbooking.repository;

import java.time.LocalDate;
import java.util.List;

import com.hotelbooking.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoomRepository extends JpaRepository<Room, Long> {

	@Query("SELECT DISTINCT r.roomType FROM Room r")
	List<String> findRoomTypes();
	
	   @Query(" SELECT r FROM Room r " +
	            " WHERE r.roomType LIKE %:roomType% " +
	            " AND r.id NOT IN (" +
	            "  SELECT br.room.id FROM BookedRoom br " +
	            "  WHERE ((br.checkIn <= :checkOut) AND (br.checkOut >= :checkIn))" +
	            ")")
	    List<Room> findAvailableRooms(LocalDate checkIn, LocalDate checkOut, String roomType);

}
