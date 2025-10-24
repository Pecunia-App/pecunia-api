## 0.10.1 (2025-10-24)

### Fix

- **profilepagecontrolle-and-test**: modif controller to modify profile picture
- **profilePicture-files**: add possibility to change profile picture

## 0.10.0 (2025-10-23)

### Feat

- **category-upsert-dto**: change maxLenght of category name to 20

## 0.9.0 (2025-10-22)

### Feat

- **tag**: add relation between tags and user

### Fix

- **tag**: create query to find by userId from tag instead from wallet

## 0.8.0 (2025-10-14)

### Feat

- **wallet**: get a wallet by user id
- **category-tag-provider**: withdraw pagination on get all categories, tags and providers

### Fix

- **profil-picture**: improve quality and size of profil picture
- **ci-format**: force JDK version to 21

## 0.7.0 (2025-10-13)

### Feat

- **category-tag-provider**: withdraw pagination on get all categories, tags and providers

### Fix

- **ci-format**: force JDK version to 21

## 0.6.0 (2025-10-10)

### Feat

- **transaction-category**: delete transaction endpoint + launch google format

## 0.5.0 (2025-09-26)

### BREAKING CHANGE

- Les expressions de sécurité de différentes méthodes dans différent
Controller ont été corrigés.
- Replace TransactionType column with type From Category
entity
- - ajout d'une entité Provider
- add provider to transactions dtos

### Feat

- **passwordUpdateDTO,-modify-userService**: to modify user information (test with front)
- **category**: CRUD Category entity
- **wallet-transaction**: add endpoint to retrieve pageable transactions between createdAt dates
- **provider**: create entity and dependencies

### Fix

- **PasswordUpdateDTO,-UserController**: error message same password + regex
- **modify-UserController,-PasswordUpdateDTO-and-UserService**: corrections after repo comments and bug updatePassword
- **security-expressions**: fix security expressions for some methods to have good behavior

### Refactor

- **transaction**: fix circulair references with TransactionDto with Provider Entity

## 0.4.0 (2025-08-29)

### BREAKING CHANGE

- Before this commit, a wallet cannot be delete without delete an user.
Now an user can "reset" a wallet by delete it.
- New entity in OneToOne with User entity.
A wallet cannot be create without an user attached at all.

### Feat

- **tag-transaction**: create crud tag;add tags to transaction objects
- **tag**: create repository
- **tag**: add entity
- **change-ProfilePictureService-and-ProfilePictureController**: add the possibility to change a profile picture and to delete it
- **add-ProfilePictureMapper,-ProfilePictureService,-ProfilePicture-Repository,-change-UserUpdateDTO,-GlobalExceptionHandler,-ProfilePicture,-User-and-application.properties**: add th possibility to post and get a profile picture for a user
- **Create-ProfilePicture,change-UserMapper,-User,-UserUpdateDTO,-UserService**: creation of the profilepicture entity and add the relation one to one in the user entity and modify userupdatedto usermapper userservice to adapt the profilepicture
- **data-init**: add test user in dev profile for frontend testing
- **transaction**: create entity,repository, controller, service
- **Money**: implement Money Pattern from Martin Fowler
- **Wallet**: create layers for Wallet entity ; add unit and integrations tests

### Fix

- **ProfilePictureService,-ProfilePictureController,-application.properties,-ProfilePictureControllerTest**: delete the package-lock, json and change the endpoint and replace bad request by conflict add a message for svg and modify the max width of the image

### Refactor

- **security-methods**: create meta-annotation using SpEL authorization expressions to stop writing duplicate codes
- **transaction**: add a custom validator for amount parameter in
Money Value Object to check if amount is zero or positive number
- **transaction-update**: can update created Date transaction
- **wallet-update**: wallet can be update without all parameters
- **wallet**: refine test for value obejct money;create factories for entities ; modify wallet repo test

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
