-- Insert dữ liệu cho bảng Users với 4 role, mỗi role 2 account, MK là password123
INSERT INTO [dbo].[users] (username, password, full_name, email, phone_number, gender, role, department, address, status, created_at, updated_at)
VALUES
    ('admin1', '$2y$10$.CY3UOOTx8FOb.Vhfz46ruVXFYwr8kYlcWzxcr1frFH2goXrZFgoq', 'Admin One', 'admin1@example.com', '0123456789', 'Male', 'Admin', 'HR', '123 Admin St', 'Active', GETDATE(), GETDATE()),
    ('admin2', '$2y$10$.CY3UOOTx8FOb.Vhfz46ruVXFYwr8kYlcWzxcr1frFH2goXrZFgoq', 'Admin Two', 'admin2@example.com', '0123456790', 'Female', 'Admin', 'HR', '124 Admin St', 'Active', GETDATE(), GETDATE()),

    ('recruiter1', '$2y$10$.CY3UOOTx8FOb.Vhfz46ruVXFYwr8kYlcWzxcr1frFH2goXrZFgoq', 'Recruiter One', 'recruiter1@example.com', '0123456791', 'Male', 'Recruiter', 'Recruitment', '125 Recruiter St', 'Active', GETDATE(), GETDATE()),
    ('recruiter2', '$2y$10$.CY3UOOTx8FOb.Vhfz46ruVXFYwr8kYlcWzxcr1frFH2goXrZFgoq', 'Recruiter Two', 'recruiter2@example.com', '0123456792', 'Female', 'Recruiter', 'Recruitment', '126 Recruiter St', 'Active', GETDATE(), GETDATE()),

    ('manager1', '$2y$10$.CY3UOOTx8FOb.Vhfz46ruVXFYwr8kYlcWzxcr1frFH2goXrZFgoq', 'Manager One', 'manager1@example.com', '0123456793', 'Male', 'Manager', 'Management', '127 Manager St', 'Active', GETDATE(), GETDATE()),
    ('manager2', '$2y$10$.CY3UOOTx8FOb.Vhfz46ruVXFYwr8kYlcWzxcr1frFH2goXrZFgoq', 'Manager Two', 'manager2@example.com', '0123456794', 'Female', 'Manager', 'Management', '128 Manager St', 'Active', GETDATE(), GETDATE()),

    ('interviewer1', '$2y$10$.CY3UOOTx8FOb.Vhfz46ruVXFYwr8kYlcWzxcr1frFH2goXrZFgoq', 'Interviewer One', 'interviewer1@example.com', '0123456795', 'Male', 'Interviewer', 'Interview', '129 Interviewer St', 'Active', GETDATE(), GETDATE()),
    ('interviewer2', '$2y$10$.CY3UOOTx8FOb.Vhfz46ruVXFYwr8kYlcWzxcr1frFH2goXrZFgoq', 'Interviewer Two', 'interviewer2@example.com', '0123456796', 'Female', 'Interviewer', 'Interview', '130 Interviewer St', 'Active', GETDATE(), GETDATE());

-- Insert dữ liệu cho bảng Candidates
INSERT INTO Candidates (full_name, email, phone_number, address, date_of_birth, gender, cv, current_position, skills, years_of_experience, highest_education_level, recruiter_owner_id, status, notes, created_at, updated_at)
VALUES
    ('Nguyễn Thị Mai', 'nguyenthimai@example.com', '0912345670', '20 Trần Phú, Hà Nội', '1990-01-15', 'Nữ', 'cv_mainguyen.pdf', 'Tester', '.Net, Java', 3, 'Bachelor’s Degree', 3, 'Applied', NULL, GETDATE(), GETDATE()),
    ('Trần Văn Nam', 'tranvannam@example.com', '0912345671', '10 Phạm Văn Đồng, TP.HCM', '1991-04-10', 'Nam', 'cv_namtran.pdf', 'Backend Developer', 'Java, C++', 4, 'Master Degree', 4, 'Interviewed', NULL, GETDATE(), GETDATE()),
    ('Lê Thị Hòa', 'lethihòa@example.com', '0912345672', '30 Lý Thường Kiệt, Đà Nẵng', '1992-07-25', 'Nữ', 'cv_hoale.pdf', 'Business Analyst', 'Business analysis, Communication', 5, 'Bachelor’s Degree', 3, 'Applied', NULL, GETDATE(), GETDATE()),
    ('Vũ Hoàng Anh', 'vuhoanganh@example.com', '0912345673', '40 Đinh Tiên Hoàng, Hải Phòng', '1993-09-05', 'Nam', 'cv_anhvu.pdf', 'Frontend Developer', 'JavaScript, React', 3, 'Bachelor’s Degree', 3, 'Applied', NULL, GETDATE(), GETDATE()),
    ('Phạm Minh Tâm', 'phamminhtam@example.com', '0912345674', '50 Nguyễn Văn Cừ, Cần Thơ', '1994-12-01', 'Nam', 'cv_tampham.pdf', 'Tester', 'Nodejs, .Net', 2, 'High school', 4, 'Rejected', NULL, GETDATE(), GETDATE()),
    ('Đỗ Quỳnh Hoa', 'doquynhhoa@example.com', '0912345675', '60 Trần Hưng Đạo, Hà Nội', '1995-06-10', 'Nữ', 'cv_hoado.pdf', 'Business Analyst', 'Business analysis, Java', 5, 'Bachelor’s Degree', 3, 'Interviewed', NULL, GETDATE(), GETDATE()),
    ('Lê Thị Thúy', 'lethithuy@example.com', '0912345676', '70 Nguyễn Huệ, TP.HCM', '1996-03-20', 'Nữ', 'cv_thuyle.pdf', 'Tester', 'JavaScript, Nodejs', 3, 'Master Degree', 4, 'Offered', NULL, GETDATE(), GETDATE()),
    ('Trần Hữu Quang', 'tranhuuquang@example.com', '0912345677', '80 Lê Lợi, Đà Nẵng', '1997-05-25', 'Nam', 'cv_quangtran.pdf', 'Backend Developer', 'Java, Nodejs', 4, 'PhD', 4, 'Interviewed', NULL, GETDATE(), GETDATE()),
    ('Nguyễn Minh Tân', 'nguyenminhtan@example.com', '0912345678', '90 Quang Trung, Hải Phòng', '1998-07-18', 'Nam', 'cv_tannguyen.pdf', 'Frontend Developer', 'React, JavaScript', 2, 'Bachelor’s Degree', 3, 'Applied', NULL, GETDATE(), GETDATE());

-- Insert dữ liệu cho bảng Jobs (10-20 record)
INSERT INTO Jobs (job_title, required_skills, start_date, end_date, salary_range_from, salary_range_to, working_address, benefits, level, status, description, created_at, updated_at)
VALUES
    ('Backend Developer', 'Java, Nodejs', '2023-10-01', '2023-12-31', 1000, 2000, 'Hà Nội', '25-day leave, Healthcare insurance', 'Junior', 'Open', 'Develop and maintain server-side logic.', GETDATE(), GETDATE()),
    ('Tester', '.Net, C++', '2023-10-15', '2023-12-15', 800, 1500, 'TP.HCM', 'Healthcare insurance, Hybrid working', 'Senior', 'Open', 'Ensure software quality.', GETDATE(), GETDATE()),
    ('Business Analyst', 'Business analysis, Communication', '2023-09-01', '2023-11-30', 1200, 2500, 'Đà Nẵng', 'Travel, Hybrid working', 'Manager', 'Closed', 'Analyze business needs.', GETDATE(), GETDATE()),
    ('Frontend Developer', 'JavaScript, React', '2023-09-15', '2023-11-15', 1100, 2300, 'Hải Phòng', '25-day leave, Healthcare insurance', 'Junior', 'Open', 'Develop user-facing features.', GETDATE(), GETDATE()),
    ('Project Manager', 'Leadership, Business analysis', '2023-09-05', '2023-12-05', 2000, 3500, 'Cần Thơ', '25-day leave, Hybrid working', 'Manager', 'Open', 'Oversee project execution.', GETDATE(), GETDATE()),
    ('HR Specialist', 'Communication, Negotiation', '2023-08-20', '2023-11-20', 1200, 2200, 'Hà Nội', 'Healthcare insurance', 'Senior', 'Closed', 'Manage HR tasks and hiring.', GETDATE(), GETDATE()),
    ('Tester', 'Java, .Net', '2023-10-20', '2023-12-25', 900, 1700, 'Hà Nội', '25-day leave, Healthcare insurance', 'Junior', 'Open', 'Ensure software quality.', GETDATE(), GETDATE()),
    ('Backend Developer', 'Nodejs, Java', '2023-09-01', '2023-11-01', 1500, 3000, 'Đà Nẵng', 'Hybrid working, Travel', 'Senior', 'Open', 'Maintain server-side systems.', GETDATE(), GETDATE()),
    ('Business Analyst', 'Business analysis, JavaScript', '2023-08-15', '2023-11-15', 1400, 2700, 'TP.HCM', 'Healthcare insurance, Travel', 'Manager', 'Open', 'Analyze and manage business solutions.', GETDATE(), GETDATE());

-- Insert dữ liệu cho bảng InterviewSchedules (10-20 record)
INSERT INTO Interview_Schedules (interview_title, candidate_id, job_id, schedule_date, schedule_from, schedule_to, location, recruiter_owner, meeting_id, notes, status, result, created_at, updated_at)
VALUES
    ('Backend Developer Interview', 1, 1, '2023-10-10', '09:00:00', '10:00:00', 'Zoom', 'Nguyễn Văn A', 'zoom123', NULL, 'Scheduled', NULL, GETDATE(), GETDATE()),
    ('Tester Interview', 2, 2, '2023-10-11', '14:00:00', '15:00:00', 'Office HCM', 'Trần Thị B', 'zoom124', NULL, 'Scheduled', NULL, GETDATE(), GETDATE()),
    ('Business Analyst Interview', 3, 3, '2023-10-12', '10:00:00', '11:00:00', 'Zoom', 'Lê Thị Hòa', 'zoom125', NULL, 'Scheduled', NULL, GETDATE(), GETDATE()),
    ('Frontend Developer Interview', 4, 4, '2023-10-13', '15:00:00', '16:00:00', 'Office HP', 'Vũ Hoàng Anh', 'zoom126', NULL, 'Scheduled', NULL, GETDATE(), GETDATE()),
    ('Project Manager Interview', 5, 5, '2023-10-14', '11:00:00', '12:00:00', 'Office CT', 'Phạm Minh Tâm', 'zoom127', NULL, 'Scheduled', NULL, GETDATE(), GETDATE()),
    ('HR Specialist Interview', 6, 6, '2023-10-15', '13:00:00', '14:00:00', 'Zoom', 'Đỗ Quỳnh Hoa', 'zoom128', NULL, 'Scheduled', NULL, GETDATE(), GETDATE()),
    ('Backend Developer Interview', 7, 7, '2023-10-16', '09:00:00', '10:00:00', 'Zoom', 'Lê Thị Thúy', 'zoom129', NULL, 'Scheduled', NULL, GETDATE(), GETDATE()),
    ('Tester Interview', 8, 8, '2023-10-17', '14:00:00', '15:00:00', 'Office HN', 'Trần Hữu Quang', 'zoom130', NULL, 'Scheduled', NULL, GETDATE(), GETDATE()),
    ('Business Analyst Interview', 9, 9, '2023-10-18', '10:00:00', '11:00:00', 'Zoom', 'Nguyễn Minh Tân', 'zoom131', NULL, 'Scheduled', NULL, GETDATE(), GETDATE());

-- Insert dữ liệu cho bảng Offers (10-20 record)
INSERT INTO Offers (candidate_id, job_id, approver, recruiter_owner, department, [level], contract_type, contract_period_from, contract_period_to, basic_salary, offer_status, due_date, notes, created_at, updated_at)
VALUES
    (1, 1, 3, 4, 'HR', 'Junior', 'Permanent', '2024-04-01', '2025-03-01', 60000, 'Accepted', '2024-03-01', 'Full-time contract', GETDATE(), GETDATE()),
    (2, 2, 4, 2, 'IT', 'Junior', 'Contract', '2024-04-01', '2025-03-01', 50000, 'Rejected', '2024-04-01', '6-month contract', GETDATE(), GETDATE()),
    (3, 3, 5, 2, 'HR', 'Junior', 'Permanent', '2024-04-01', '2025-03-01', 70000, 'Waiting for Approval', '2024-05-01', 'Full-time contract', GETDATE(), GETDATE()),
    (4, 4, 6, 3, 'IT', 'Junior', 'Trial', '2024-04-01', '2025-03-01', 45000, 'Approved', '2024-06-01', '3-month trial', GETDATE(), GETDATE()),
    (5, 5, 7, 3, 'HR', 'Junior', 'Permanent', '2024-04-01', '2025-03-01', 75000, 'Declined', '2024-07-01', 'Full-time contract', GETDATE(), GETDATE()),
    (6, 6, 8, 3, 'IT', 'Junior', 'Permanent', '2024-04-01', '2025-03-01', 65000, 'Cancelled', '2024-08-01', 'Full-time contract', GETDATE(), GETDATE()),
    (7, 7, 3, 2, 'HR', 'Junior', 'Contract', '2024-04-01', '2025-03-01', 70000, 'Accepted', '2024-09-01', '1-year contract', GETDATE(), GETDATE()),
    (8, 8, 4, 2, 'IT', 'Junior', 'Permanent', '2024-04-01', '2025-03-01', 80000, 'Accepted', '2024-10-01', 'Full-time contract', GETDATE(), GETDATE()),
    (9, 9, 5, 3, 'HR', 'Junior', 'Trial', '2024-04-01', '2025-03-01', 50000, 'Rejected', '2024-11-01', '3-month trial', GETDATE(), GETDATE())
