## 0.3.0 (2025-08-10)

### Feat

- **user-DTOs**: add annotations on dtos classes. Improve regex on registration (no space and specifics caracters)
- **user-and-auth-controllers**: add tags on classes and methods to describe requests
- **Security-configuration**: upgrade the openapi version in dependencies. Delete useless environment variables and update docs_path. Allow requests on swagger in security config class

### Fix

- **UserRegistrationDTO**: Put min and max size assertions on first and lastname. Improve the regex on those fields with letters and no numbers
- **env**: modify .env-example to configure application.properties to dev
- **ci**: modify trigger workflows for GA

### Refactor

- **ci**: fix format workflows

## 0.2.0 (2025-07-11)

### Feat

- **user-service**: improve the update user: create a user update dto for better control
- **user-controller-and-user-controller-test**: add two custom methods search by first and lastname. And add test on user controller
- add User class, controller, mapper. Also add the authentication and jwt services
- **auth-controller**: add auth controller and test registration with postman and it works
- **User-service**: add user service then implement the password encoder from security confi
- **User-repository**: add the user repository interface
- **User-class**: add the user entity and that all
- **cd**: create workflow to deploy backend

### Fix

- **cicd**: create a deploy script which is on the server

## 0.1.0 (2025-06-24)

### Feat

- create a sercet database environment and explain into the readme how to configure it
- add commitizen ; pre-commit config to check commits before commit command
