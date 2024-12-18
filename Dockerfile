# Sử dụng một hình ảnh OpenJDK Java 17 làm cơ sở
FROM openjdk:17-jdk

# working directory
WORKDIR /be

# Sao chép từ máy tính của bạn (PC, laptop) vào container
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Cài đặt dos2unix
RUN #apt-get update && apt-get install -y dos2unix

# Chuyển đổi line endings của file mvnw
RUN #dos2unix mvnw
# Sao chép các tệp còn lại
COPY src ./src

# Chạy lệnh trong container
CMD ["./mvnw", "spring-boot:run"]