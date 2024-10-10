-- Create a new database called 'InterviewManagementProjectDB'
-- Connect to the 'master' database to run this snippet
USE master
GO
-- Create the new database if it does not exist already
IF NOT EXISTS (
    SELECT [name]
        FROM sys.databases
        WHERE [name] = N'InterviewManagementProjectDB'
)
CREATE DATABASE InterviewManagementProjectDB
GO

-- Create table Users
CREATE TABLE Users (
    user_id INT PRIMARY KEY IDENTITY(1,1),
    username NVARCHAR(100) NOT NULL UNIQUE,
    password NVARCHAR(255) NOT NULL,
    full_name NVARCHAR(150) NOT NULL,
    email NVARCHAR(150) NOT NULL UNIQUE,
    phone_number NVARCHAR(15),
    role NVARCHAR(50) CHECK (role IN ('Admin', 'Recruiter', 'Manager', 'Interviewer')),
    status NVARCHAR(50) CHECK (status IN ('Active', 'Inactive')),
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE()
)
GO

-- Create table Candidates
CREATE TABLE Candidates (
    candidate_id INT PRIMARY KEY IDENTITY(1,1),
    full_name NVARCHAR(150) NOT NULL,
    email NVARCHAR(150) NOT NULL UNIQUE,
    phone_number NVARCHAR(15),
    address NVARCHAR(255),
    date_of_birth DATE,
    gender NVARCHAR(50) CHECK (gender IN ('Male', 'Female', 'Others')),
    cv NVARCHAR(255),
    current_position NVARCHAR(100),
    skills NVARCHAR(255),
    years_of_experience INT,
    highest_education_level NVARCHAR(100),
    recruiter_owner_id INT FOREIGN KEY REFERENCES Users(user_id),
    status NVARCHAR(50) CHECK (status IN ('Open', 'Waiting for Interview', 'Passed', 'Failed', 'Banned', 'Cancelled Interview', 'Waiting for Approval', 'Approved Offer', 'Rejected Offer', 'Waiting for Response', 'Accepted Offer', 'Declined Offer')),
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE()
)
GO

-- Create table Jobs
CREATE TABLE Jobs (
    job_id INT PRIMARY KEY IDENTITY(1,1),
    job_title NVARCHAR(150) NOT NULL,
    required_skills NVARCHAR(255),
    start_date DATE,
    end_date DATE,
    salary_range_from DECIMAL(18,2),
    salary_range_to DECIMAL(18,2),
    working_address NVARCHAR(255),
    benefits NVARCHAR(255),
    level NVARCHAR(255) NOT NULL,
    status NVARCHAR(50) CHECK (status IN ('Draft', 'Open', 'Closed')),
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE()
)
GO

-- Create table InterviewSchedules
CREATE TABLE InterviewSchedules (
    schedule_id INT PRIMARY KEY IDENTITY(1,1),
    interview_title NVARCHAR(150),
    candidate_id INT FOREIGN KEY REFERENCES Candidates(candidate_id),
    job_id INT FOREIGN KEY REFERENCES Jobs(job_id),
    schedule_date DATE,
    schedule_from TIME,
    schedule_to TIME,
    location NVARCHAR(255),
    interviewer_id INT FOREIGN KEY REFERENCES Users(user_id),
    status NVARCHAR(50) CHECK (status IN ('New', 'Invited', 'Interviewed', 'Cancelled')),
    result NVARCHAR(50) CHECK (result IN ('Passed', 'Failed', 'N/A')),
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE()
)
GO

-- Create table Offers
CREATE TABLE Offers (
    offer_id INT PRIMARY KEY IDENTITY(1,1),
    candidate_id INT FOREIGN KEY REFERENCES Candidates(candidate_id),
    job_id INT FOREIGN KEY REFERENCES Jobs(job_id),
    approved_by INT FOREIGN KEY REFERENCES Users(user_id),
    contract_type NVARCHAR(50) CHECK (contract_type IN ('Trial', 'Permanent', 'Contract')),
    salary DECIMAL(18,2),
    offer_status NVARCHAR(50) CHECK (offer_status IN ('Waiting for Approval', 'Approved', 'Rejected', 'Accepted', 'Declined', 'Cancelled')),
    due_date DATE,
    notes NVARCHAR(500),
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE()
)
GO

-- Create table Logs
CREATE TABLE Logs (
    log_id INT PRIMARY KEY IDENTITY(1,1),
    action NVARCHAR(255),
    user_id INT,
    entity_type NVARCHAR(50),
    entity_id INT,
    description NVARCHAR(500),
    timestamp DATETIME DEFAULT GETDATE()
)
GO

-- Insert dữ liệu cho bảng Users với 4 role, mỗi role 2 account, MK là password123
INSERT INTO Users (username, password, full_name, email, phone_number, role, status, created_at, updated_at)
VALUES 
('admin1', '$2y$10$.CY3UOOTx8FOb.Vhfz46ruVXFYwr8kYlcWzxcr1frFH2goXrZFgoq', 'Admin One', 'admin1@example.com', '0123456789', 'Admin', 'Active', GETDATE(), GETDATE()),
('admin2', '$2y$10$.CY3UOOTx8FOb.Vhfz46ruVXFYwr8kYlcWzxcr1frFH2goXrZFgoq', 'Admin Two', 'admin2@example.com', '0123456790', 'Admin', 'Active', GETDATE(), GETDATE()),

('recruiter1', '$2y$10$.CY3UOOTx8FOb.Vhfz46ruVXFYwr8kYlcWzxcr1frFH2goXrZFgoq', 'Recruiter One', 'recruiter1@example.com', '0123456791', 'Recruiter', 'Active', GETDATE(), GETDATE()),
('recruiter2', '$2y$10$.CY3UOOTx8FOb.Vhfz46ruVXFYwr8kYlcWzxcr1frFH2goXrZFgoq', 'Recruiter Two', 'recruiter2@example.com', '0123456792', 'Recruiter', 'Active', GETDATE(), GETDATE()),

('manager1', '$2y$10$.CY3UOOTx8FOb.Vhfz46ruVXFYwr8kYlcWzxcr1frFH2goXrZFgoq', 'Manager One', 'manager1@example.com', '0123456793', 'Manager', 'Active', GETDATE(), GETDATE()),
('manager2', '$2y$10$.CY3UOOTx8FOb.Vhfz46ruVXFYwr8kYlcWzxcr1frFH2goXrZFgoq', 'Manager Two', 'manager2@example.com', '0123456794', 'Manager', 'Active', GETDATE(), GETDATE()),

('interviewer1', '$2y$10$.CY3UOOTx8FOb.Vhfz46ruVXFYwr8kYlcWzxcr1frFH2goXrZFgoq', 'Interviewer One', 'interviewer1@example.com', '0123456795', 'Interviewer', 'Active', GETDATE(), GETDATE()),
('interviewer2', '$2y$10$.CY3UOOTx8FOb.Vhfz46ruVXFYwr8kYlcWzxcr1frFH2goXrZFgoq', 'Interviewer Two', 'interviewer2@example.com', '0123456796', 'Interviewer', 'Active', GETDATE(), GETDATE());

-- Insert dữ liệu cho bảng Candidates
INSERT INTO Candidates (full_name, email, phone_number, address, date_of_birth, gender, cv, current_position, skills, years_of_experience, highest_education_level, recruiter_owner_id, status, created_at, updated_at)
VALUES 
('Nguyễn Văn A', 'nguyenvana@example.com', '0987654321', '123 Đường Lê Lợi', '1990-05-10', 'Male', 'cv1.pdf', 'Kỹ sư phần mềm', 'Java, SQL', 5, 'Cử nhân', 3, 'Open', GETDATE(), GETDATE()),
('Trần Thị B', 'tranthib@example.com', '0987654322', '456 Đường Trần Hưng Đạo', '1992-07-15', 'Female', 'cv2.pdf', 'Nhân viên phân tích dữ liệu', 'Python, SQL', 4, 'Cử nhân', 3, 'Waiting for Interview', GETDATE(), GETDATE()),
('Lê Văn C', 'levanc@example.com', '0987654323', '789 Đường Phạm Văn Đồng', '1985-02-20', 'Male', 'cv3.pdf', 'Quản lý dự án', 'Leadership, Agile', 10, 'Thạc sĩ', 4, 'Passed', GETDATE(), GETDATE()),
('Phạm Thị D', 'phamthid@example.com', '0987654324', '321 Đường Nguyễn Huệ', '1993-11-22', 'Female', 'cv4.pdf', 'Lập trình viên web', 'HTML, CSS, JavaScript', 3, 'Cử nhân', 4, 'Failed', GETDATE(), GETDATE()),
('Hoàng Văn E', 'hoangvane@example.com', '0987654325', '654 Đường Hai Bà Trưng', '1988-04-12', 'Male', 'cv5.pdf', 'Kỹ sư DevOps', 'AWS, Jenkins', 6, 'Cử nhân', 5, 'Banned', GETDATE(), GETDATE()),
('Đỗ Thị F', 'dothif@example.com', '0987654326', '987 Đường Võ Thị Sáu', '1991-03-17', 'Female', 'cv6.pdf', 'Nhà thiết kế UI/UX', 'Figma, Adobe XD', 5, 'Cử nhân', 5, 'Cancelled Interview', GETDATE(), GETDATE()),
('Vũ Văn G', 'vuvang@example.com', '0987654327', '159 Đường Nam Kỳ Khởi Nghĩa', '1986-09-25', 'Male', 'cv7.pdf', 'Kỹ sư mạng', 'Cisco, Networking', 8, 'Cử nhân', 6, 'Waiting for Approval', GETDATE(), GETDATE()),
('Lý Thị H', 'lythih@example.com', '0987654328', '753 Đường Nguyễn Văn Linh', '1990-12-05', 'Female', 'cv8.pdf', 'Trưởng phòng nhân sự', 'HR, Tuyển dụng', 7, 'Thạc sĩ', 6, 'Approved Offer', GETDATE(), GETDATE()),
('Phan Văn I', 'phanvani@example.com', '0987654329', '852 Đường Nguyễn Trãi', '1984-08-30', 'Male', 'cv9.pdf', 'Lập trình viên backend', 'Java, Spring', 6, 'Cử nhân', 7, 'Rejected Offer', GETDATE(), GETDATE()),
('Dương Thị K', 'duongthik@example.com', '0987654330', '951 Đường Lê Đại Hành', '1989-06-14', 'Female', 'cv10.pdf', 'Kỹ sư kiểm thử phần mềm', 'Selenium, TestNG', 5, 'Cử nhân', 7, 'Waiting for Response', GETDATE(), GETDATE());

-- Insert dữ liệu cho bảng Jobs (10-20 record)
INSERT INTO Jobs (job_title, required_skills, start_date, end_date, salary_range_from, salary_range_to, working_address, benefits, level, status, created_at, updated_at)
VALUES 
('Software Engineer', 'Java, SQL', '2024-01-01', '2024-03-01', 50000, 70000, 'Company HQ', 'Health Insurance, Bonus', 'Junior', 'Open', GETDATE(), GETDATE()),
('Data Analyst', 'Python, SQL', '2024-02-01', '2024-04-01', 45000, 65000, 'Company HQ', 'Health Insurance, Bonus', 'Junior', 'Open', GETDATE(), GETDATE()),
('Project Manager', 'Leadership, Agile', '2024-03-01', '2024-06-01', 60000, 85000, 'Remote', 'Health Insurance, Bonus', 'Senior', 'Closed', GETDATE(), GETDATE()),
('Web Developer', 'HTML, CSS, JavaScript', '2024-01-15', '2024-04-15', 40000, 60000, 'Remote', 'Health Insurance, Bonus', 'Junior', 'Draft', GETDATE(), GETDATE()),
('DevOps Engineer', 'AWS, Jenkins', '2024-05-01', '2024-07-01', 55000, 75000, 'Company HQ', 'Health Insurance, Bonus', 'Senior', 'Open', GETDATE(), GETDATE()),
('UI/UX Designer', 'Figma, Adobe XD', '2024-04-01', '2024-06-01', 50000, 70000, 'Company HQ', 'Health Insurance, Bonus', 'Junior', 'Open', GETDATE(), GETDATE()),
('Network Engineer', 'Cisco, Networking', '2024-03-15', '2024-06-15', 55000, 75000, 'Company HQ', 'Health Insurance, Bonus', 'Senior', 'Open', GETDATE(), GETDATE()),
('HR Manager', 'HR, Recruiting', '2024-05-01', '2024-08-01', 60000, 90000, 'Remote', 'Health Insurance, Bonus', 'Senior', 'Open', GETDATE(), GETDATE()),
('Backend Developer', 'Java, Spring', '2024-01-10', '2024-04-10', 55000, 80000, 'Company HQ', 'Health Insurance, Bonus', 'Junior', 'Open', GETDATE(), GETDATE()),
('QA Engineer', 'Selenium, TestNG', '2024-02-10', '2024-05-10', 45000, 65000, 'Remote', 'Health Insurance, Bonus', 'Junior', 'Open', GETDATE(), GETDATE());

-- Insert dữ liệu cho bảng InterviewSchedules (10-20 record)
INSERT INTO InterviewSchedules (interview_title, candidate_id, job_id, schedule_date, schedule_from, schedule_to, location, interviewer_id, status, result, created_at, updated_at)
VALUES 
('John Doe Interview', 1, 1, '2024-01-15', '10:00', '11:00', 'Company HQ', 7, 'Invited', 'Passed', GETDATE(), GETDATE()),
('Jane Smith Interview', 2, 2, '2024-01-18', '14:00', '15:00', 'Company HQ', 8, 'Interviewed', 'Failed', GETDATE(), GETDATE()),
('Mike Johnson Interview', 3, 3, '2024-02-01', '09:00', '10:00', 'Remote', 7, 'Cancelled', 'N/A', GETDATE(), GETDATE()),
('Anna Brown Interview', 4, 4, '2024-02-05', '13:00', '14:00', 'Remote', 8, 'Interviewed', 'Passed', GETDATE(), GETDATE()),
('Chris Green Interview', 5, 5, '2024-03-10', '11:00', '12:00', 'Company HQ', 7, 'Invited', 'Failed', GETDATE(), GETDATE()),
('Lisa White Interview', 6, 6, '2024-03-15', '15:00', '16:00', 'Company HQ', 8, 'New', 'N/A', GETDATE(), GETDATE()),
('James Black Interview', 7, 7, '2024-02-20', '10:00', '11:00', 'Remote', 7, 'Interviewed', 'Passed', GETDATE(), GETDATE()),
('Sarah Blue Interview', 8, 8, '2024-04-05', '14:00', '15:00', 'Remote', 8, 'Interviewed', 'Passed', GETDATE(), GETDATE()),
('David Red Interview', 9, 9, '2024-01-22', '09:00', '10:00', 'Company HQ', 7, 'Interviewed', 'Failed', GETDATE(), GETDATE()),
('Nancy Yellow Interview', 10, 10, '2024-02-15', '16:00', '17:00', 'Remote', 8, 'Interviewed', 'Passed', GETDATE(), GETDATE());

-- Insert dữ liệu cho bảng Offers (10-20 record)
INSERT INTO Offers (candidate_id, job_id, approved_by, contract_type, salary, offer_status, due_date, notes, created_at, updated_at)
VALUES 
(1, 1, 3, 'Permanent', 60000, 'Accepted', '2024-03-01', 'Full-time contract', GETDATE(), GETDATE()),
(2, 2, 4, 'Contract', 50000, 'Rejected', '2024-04-01', '6-month contract', GETDATE(), GETDATE()),
(3, 3, 5, 'Permanent', 70000, 'Waiting for Approval', '2024-05-01', 'Full-time contract', GETDATE(), GETDATE()),
(4, 4, 6, 'Trial', 45000, 'Approved', '2024-06-01', '3-month trial', GETDATE(), GETDATE()),
(5, 5, 7, 'Permanent', 75000, 'Declined', '2024-07-01', 'Full-time contract', GETDATE(), GETDATE()),
(6, 6, 8, 'Permanent', 65000, 'Cancelled', '2024-08-01', 'Full-time contract', GETDATE(), GETDATE()),
(7, 7, 3, 'Contract', 70000, 'Accepted', '2024-09-01', '1-year contract', GETDATE(), GETDATE()),
(8, 8, 4, 'Permanent', 80000, 'Accepted', '2024-10-01', 'Full-time contract', GETDATE(), GETDATE()),
(9, 9, 5, 'Trial', 50000, 'Rejected', '2024-11-01', '3-month trial', GETDATE(), GETDATE()),
(10, 10, 6, 'Permanent', 55000, 'Approved', '2024-12-01', 'Full-time contract', GETDATE(), GETDATE());
