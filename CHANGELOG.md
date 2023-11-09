# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.6] (2023-11-10)
- Added logic to prohibit a client from making multiple bookings in same month with same group, client id, and booking person
- Added exception handler for multiple bookings
- Added UT to handle no booking for a specific client id

## [1.0.5] (2023-11-09)
- Added logic for allowable days for booking
- Added restriction of booking within weeks configured in application.yaml

## [1.0.4] (2023-11-07)
- Added logic for handling reservation in availability_calendar table
  - Added handler for pass date
  - Added handler for checking if room is available or not on a given date
  - Added AvailableRoomOnDateRepositoryImpl, AvailableDateRepository, AvailableRoomOnDateCustom, AvailableRoomOnDateRepository

## [1.0.3] (2023-11-06)
- Added JavaDoc as description to some classes
- Clean-up code
- Added UTs
- Modified pom.xml

## [1.0.2] (2023-11-03)
- Added JWT Authentication and Spring Security
- Added the ff. Java Classes:
  - JwtAuthenticationFilter
  - SecurityConfig
  - dao package
  - Extended UserDetails in UserEntity
  - UserAuthenticationResources
  - AuthenticationService
  - JwtService
- Modified the responses of resources
- Added CSV report template

## [1.0.1] (2023-11-01)
- Enabled OpenAPI Swagger UI
- Added Booking-Manager-API-1.0.0.yaml file
- Added Reservation Resources endpoint for booking
- Added some UTs

## [1.0.0] (2023-10-25)
- Created initial logic for booking manager
- TODO: Add UTs