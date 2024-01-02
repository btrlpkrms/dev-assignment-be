package com.transferz.dao;

import lombok.*;

import javax.persistence.*;

/**
 * Entity representing a passenger associated with a flight.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Passenger
{

	/**
	 * The unique identifier for the passenger.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * The name of the passenger.
	 */
	@Column(nullable = false)
	private String name;

	/**
	 * The flight with which the passenger is associated.
	 * This establishes a many-to-one relationship between passengers and a flight.
	 */
	@ManyToOne
	@JoinColumn(name= "flight_id",nullable = false)
	private Flight flight;

}
