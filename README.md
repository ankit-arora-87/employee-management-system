# employee-management-system

### It provides REST API to add, update, view, list & upload employees data using below endpoints:
  - POST /api/v1/users
  - PUT /api/v1/users/e0002
  - GET /api/v1/users/e0002
  - GET /api/v1/users?page=0&limit=10&sortField=login&sortOrder=ASC&minSalary=300&maxSalary=10000
  - DELETE /api/v1/users/e0002
  - POST /api/v1/users/upload
    - Uploading the CSV to local server
    - Then read the data from csv file from local server
    - Validate the records as per the acceptance criteria & discard the upload of data to database if any 1 of validation fails

### Prerequisites to run this API in your local environment:
  - Must have Java 11
  - Must have Maven installed (to download required dependencies)
  - Install all mvn dependencies & adjust below path <ABSOLUTE_PATH> in respective files:
    - src/main/resources/application.properties 
      - storage.upload.path=<ABSOLUTE_PATH>/employee-management-system/employee-service/src/main/resources/uploads/
      - eg: storage.upload.path=/Users/ankitarora/projects/employee-management-system/employee-service/src/main/resources/uploads/

    - src/test/java/com/ems/userservice/UserServiceApplicationTests.java [please make sure to use valid csv file with valid employees data]
      - private String EMPLOYEE_CSV_PATH = "<ABSOLUTE_PATH>/employee-management-system/employee-service/src/main/resources/uploads/employees-data.csv";
      - eg: "/Users/ankitarora/projects/employee-management-system/employee-service/src/main/resources/uploads/employees-data.csv";

  - Run the src/test/java/com/ems/userservice/UserServiceApplicationTests.java file to run & validate the test cases: 
      - /api/v1/products/search?query=apple&page=0&size=1&sortBy=name

  - Run the src/main/java/com/ems/userservice/UserServiceApplication.java file to run the application in action & you can validate all the required scenarios as per the user stories.


### Designed database reference ERD
  - ems_erd.pdf (you can find this ERD in root directory) [Also added employee_audit_logs table to track each and every action on the employess data either by file upload or by manual user action]
    
### Local API testing reference screenshots
  - API Local Testing Reference.pdf (you can find this PDF in root directory)

### Added all versions of CSV with valid and invalid use cases (you can find these CSV's in root directory)
  - valid-employees-with-skip-data.csv
  - valid-employees-data.csv
  - invalid-start-date-employees-data.csv
  - invalid-salary-employees-data.csv
  - invalid-login-employees-data.csv
  - invalid-employee-id-employees-data.csv
  
### Suggestions 
  - We can use spring boot batch processing dependency to handle large data set & keep that service deployed on different server as it might take more CPU & memory while handling large data sets (https://spring.io/guides/gs/batch-processing/)
  - Using spring boot batch processing, we can handle data in chunks & manage global state of file upload progress so no other user (HR or Accounts) person can re-upload the file until the existing import is in progress.
  - Instead of discarding the complete upload for failing one of the validations, we can just by pass those invalid records (store those invalid records in hashmap) & proceed with the correct ones. At last we can just write those invalid records to csv
again & send them back via email or push notification with link to download that file.
  - Currently the upload API is synchronous in nature, it'll be better to use asynchronous way to handle all the respective actions and notify user to their dashboard via push notifications or emails for the success or failed events.




