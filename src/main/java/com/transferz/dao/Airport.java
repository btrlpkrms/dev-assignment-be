package com.transferz.dao;

import lombok.*;

import javax.persistence.*;

/**
 * Entity representing an airport in the system.
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Airport
{
	/**
	 * The unique identifier for the airport.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * The name of the airport.
	 */
	@Column(nullable = false)
	private String name;

	/**
	 * This is a unique attribute and is used to identify the airport.
	 */
	@Column(nullable = false,unique = true)
	private String code;

	/**
	 * The country in which the airport is located.
	 */
	@Column(nullable = false)
	private String country;
}
