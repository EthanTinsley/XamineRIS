-- insert data for basic XamineRIS database -- 

INSERT INTO permission VALUES (1 , "ReferringDr", "RefDr" ) ;
INSERT INTO permission VALUES (2 , "Receptionist", "Recep" ) ;
INSERT INTO permission VALUES (3 , "Technician", "Tech" ) ;
INSERT INTO permission VALUES (4 , "Radiologist", "Radio" ) ;
INSERT INTO permission VALUES (5 , "SuperUser", "Sup" ) ;

Select * FROM permission ;

INSERT INTO team VALUES ( 001 , "TechnicalTeam01");
INSERT INTO team VALUES ( 002 , "RadiologistTeam01");

Select * From team ;

INSERT INTO user VALUES ("jdoctor01" , 1 ,"Jeff", "Doctorman", "password","jDoctorman@ung.edu" , "RefDr", null , true, true, false); 
INSERT INTO user VALUES ("jdoctor02" , 1 , "Brittney", "Doctorwoman", "password","jDoctorwoman@ung.edu" , "RefDr", null ,true, true, false);
INSERT INTO user VALUES ("trecep01" , 2 , "Ted", "Recep", "password","pTechson@ung.edu" , "Recep", null, true, true, false); 
INSERT INTO user VALUES ("ptechson01" , 3 ,"Polly", "Techson", "password", "tRecep@ung.edu" , "Tech", null, true, true, false); 
INSERT INTO user VALUES ("iraddon01" , 4 ,"Innis", "Raddon", "password","iRaddon@ung.edu" , "Radio",null, true, true, false); 
INSERT INTO user VALUES ("psuper01" , 5, "Pa", "Super", "password", "pSuper@ung.edu" , "Sup", null , true, true, true); 

Select * FROM user ;
-- Date formatting == YYYY-MM-DD
INSERT INTO patient VALUES (1 , "jdoctor01" , "Mathew" , "McCoy" , "mmcoyboi@notung.edu" , "1990-01-05" , "Male" , true , false , true , "" , null ) ;
INSERT INTO patient VALUES (2 , "jdoctor01" , "Jim" , "Halpert" , "Tuna@notung.edu" , "1980-10-15" , "Male" , false , false , false , "" , null ) ;
INSERT INTO patient VALUES (3 , "jdoctor01" , "Pam" , "Halpert" , "Mrs.Tuna@notung.edu" , "1984-03-03" , "Female" , false , false , false , "" , null ) ;
INSERT INTO patient VALUES (4 , "jdoctor02" , "Dwight" , "Schrute" , "schruteFarms@notung.edu" , "1975-12-10" , "Male" , false , false , false , "" , null ) ;
INSERT INTO patient VALUES (5 , "jdoctor02" , "Stanley" , "Hudson" , "SHudson@notung.edu" , "1950-07-23" , "Male" , false , true , false , "" , null ) ;
INSERT INTO patient VALUES (6 , "jdoctor02" , "Micheal" , "Scott" , "TheManager@notung.edu" , "1972-04-19" , "Male" , true , false , false , "" , null ) ;

Select * from patient ;

INSERT INTO Modality VALUES ( 701 , "X-Ray" , true , "rm701" ) ;
INSERT INTO Modality VALUES ( 555 , "MRI-Scan", false , "rm555" ) ;
INSERT INTO Modality VALUES ( 423 , "CAT-scan" , false , "rm423" ) ;
INSERT INTO Modality VALUES ( 702 , "X-Ray" , false , "rm702" ) ;

Select * from Modality  ;

-- date time formatting YYYY-MM-DD hh:mm:ss xm 
INSERT INTO imagingorder VALUES ( 1 , 1 , "Unscheduled" , null , null, "X-ray needed!" , "X-Ray" , null , null, null, null) ;
INSERT INTO imagingorder VALUES ( 2 , 6 , "scheduled" , "2021-04-02", "10:45:00" , "Due to some serious issues with patient arm we think it may be broken" , "X-Ray" , null , null, null, null) ;
INSERT INTO imagingorder VALUES ( 3,  5 , "Checked-in" , "2021-04-02", "10:30:00" , "I beleive patient needs an MRI" , "MRI" , null , null, null, null) ;
INSERT INTO imagingorder VALUES ( 4 , 3 , "Scheduled" , "2021-10-21", "10:45:00" , "I think the patient needs a Cat scan " , "CAT scan " , null , null, null, null) ;

Select * From imagingorder ;

-- not sure how to uplodad blobs into database, but they will store the actual image 
INSERT INTO image VALUES (001 ,4, "Initial Image" , "2021-04-02 10:48:23" , null) ;
INSERT INTO image VALUES (002 ,4, "Second  Image" , "2021-04-02 10:52:33" , null) ;
INSERT INTO image VALUES (003 ,4, "Third Image" , "2021-04-02 11:05:28" , null) ;
INSERT INTO image VALUES (004 ,4, "Fourth  Image" , "2021-04-02 11:10:44" , null) ;
INSERT INTO image VALUES (005 ,4, "Final  Image" , "2021-04-02 11:12:25" , null) ;

Select * From image ;
