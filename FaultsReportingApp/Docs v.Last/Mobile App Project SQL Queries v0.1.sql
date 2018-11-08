--Creating the citiezen user
Create proc Mob_Citizen_User_Profile
(@role_ID int,
 @name varchar(25),
 @surname varchar(25), 
 @gender char(1),
 @contact char(25),
 @email varchar(30),
 @u_password varchar(20),
 @m_date char(25)
 )
 as
 begin
 Insert into Mob_Person (role_ID, name, surname, gender, contact, email, u_password, m_date)
 values (@role_ID,@name,@surname,@gender,@contact, @email,@u_password,@m_date)
 end
 GO
 
 --Creating the incident
 Create proc Mob_New_Create_Incident
(
-- Location table
 @street varchar(50),
 @city varchar(20),
 @postCode int,
 @dateS date,
-- Incident table
 @dept_ID int,
 @person_ID int,
 @status_ID int,
 @inc_Desc varchar(max),
 @inc_Date date
 )
 as
 begin
 -- Location table
 insert into Mob_Location (street,city,postCode,dateS)
 values (@street,@city,@postCode,@dateS);
 -- Incident table
 insert into Mob_Incident (dept_ID, person_ID, location_ID, picture_ID, status_ID, inc_Desc, inc_Date)
 values (@dept_ID, @person_ID, (select MAX(location_ID) from Mob_Location), (select MAX(picture_ID) from Mob_Picture), @status_ID, @inc_Desc,  @inc_Date)
 end
 GO
 
 
 
 select MAX(location_ID) from Mob_Location
 select * from Mob_Picture
 
