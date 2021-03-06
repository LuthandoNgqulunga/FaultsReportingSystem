Use hon04
GO

Create table Mob_User_Role
(role_ID int identity(1,1) not null,
 role_Desc varchar(25) not null,
 primary key (role_ID)
 )
 GO

 Create table Mob_Person
 (person_ID int identity(1,1) not null,
  role_ID int not null,
  name varchar(25) not null,
  surname varchar(25) not null,
  contact char(25) not null,
  email varchar(30) not null,
  u_password varchar(20) NOT NULL,
  gender char(1) not null,
  m_date char(25) NOT NULL
  primary key(person_ID),
  foreign key (role_ID) references Mob_User_Role (role_ID)
  )
  GO

 Create table Mob_Emp_Department
 (dept_ID int identity(1,1) not null,
  dept_Name varchar(25) not null,
  primary key (dept_ID)
  )
  GO
  
 Create table Mob_Employee
 (person_ID int not null,
  dept_ID int not null,
  foreign key (person_ID) references Mob_Person (person_ID),
  foreign key (dept_ID) references Mob_Emp_Department (dept_ID)
 )
 GO

 Create table Mob_Incid_Status
 (status_ID int identity(1,1) not null,
  status_Desc varchar(10) not null,
  primary key (status_ID)
 )
 GO

 Create table Mob_Picture
 (picture_ID int identity(1,1) not null,
  picture varchar(max),
  pdate date not null,
  primary key (picture_ID)
 )
 GO

 Create table Mob_Location
 (location_ID int identity(1,1) not null,
  street varchar(50),
  city varchar(20),
  postCode int not null,
  dateS date not null,
  primary key (location_ID)
 )
 GO

 Create table Mob_Incident
 (incident_ID int identity(1,1) not null,
  dept_ID int not null,
  person_ID int not null,
  location_ID int not null,
  picture_ID int not null,
  status_ID int not null,
  inc_Desc varchar(max) not null,
  inc_Date date not null,
  primary key (incident_ID),
  foreign key (dept_ID) references Mob_Emp_Department (dept_ID),
  foreign key (person_ID) references Mob_Person (person_ID),
  foreign key (location_ID) references Mob_Location (location_ID),
  foreign key (picture_ID) references Mob_Picture (picture_ID),
  foreign key (status_ID) references Mob_Incid_Status (status_ID)
  )
  GO
 
 Create table Mob_Issue_ReportNotification
 (notification_ID int identity(1,1) not null,
  incident_ID int not null,
  person_ID int not null,
  message_Body varchar(max) not null,
  notif_Date date not null,
  primary key (notification_ID),
  foreign key (incident_ID) references Mob_Incident (incident_ID),
  foreign key (person_ID) references Mob_Person (person_ID)
  )
  GO

  Create table Mob_Audit_Trail
  (audit_ID int identity(1,1) not null,
   person_ID int not null,
   incident_ID int not null,
   status_ID int not null,
   comment varchar(max) not null,
   aud_Date date not null,
   primary key (audit_ID),
   foreign key (person_ID) references Mob_Person (person_ID),
   foreign key (incident_ID) references Mob_Incident (incident_ID),
   foreign key (status_ID) references Mob_Incid_Status (status_ID)
   )
   GO






  


