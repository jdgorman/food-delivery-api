# Change Log
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/)
and this project adheres to [Semantic Versioning](http://semver.org/).

## [0.0.5] - 2026-02-11

### Added

- CustomerOrder and OrderItem entities with persistence and validation.
- Customer order status enum and order-related DTOs.
- Customer order service and order controller endpoints under `/api/orders`.
- Repository tests for orders and order items plus service/controller tests for order flows.

### Changed

- Updated README with order management endpoints, relationships, and project structure.

## [0.0.4] - 2026-02-10

### Added

- Customer entity, repository, service, and controller.
- DeliveryAddress entity, repository, service, and controller.
- Postman collection for testing API endpoints.
- Updated README with API documentation and usage instructions.
- CRUD operations for Customer and DeliveryAddress.
- Builder annotation for entities and DTOs to simplify object creation.
- Validation for Customer and DeliveryAddress inputs.

## [0.0.3] - 2026-02-06

### Added

- MenuItem entity, repository, service, and controller.
- Basic CRUD operations for MenuItem.
- Association between Restaurant and MenuItem.
- Validation for MenuItem inputs.
- CheckStyle analysis for code quality enforcement.
- JavaDoc comments.

## [0.0.2] - 2026-02-02

### Added

- CI and CD workflows for automated testing and deployment.

## [0.0.1] - 2026-01-27

### Added

- This CHANGELOG file and changes.xml for tracking versions.
- Initial project setup and structure.
- Restaurant entity, repository, service, and controller.
- Basic CRUD operations for Restaurant.
- Exception handling for Restaurant not found scenarios.