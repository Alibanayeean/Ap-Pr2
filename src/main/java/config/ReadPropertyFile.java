package config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ReadPropertyFile {
    static Properties prop;
    static FileInputStream fileInputStream;

    public static String passStringFromConfigFile(ConfigIdentifier configIdentifier){
        try {
            fileInputStream = new FileInputStream("src\\main\\resources\\config.properties");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        prop = new Properties();

        try {
            prop.load(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(configIdentifier == ConfigIdentifier.IP){
            return prop.getProperty("IP");
        }
        else if(configIdentifier == ConfigIdentifier.Port){
            return prop.getProperty("PORT");
        }
        else if(configIdentifier == ConfigIdentifier.CHANGE_COLOR_OF_TEACHER){
            return prop.getProperty("CHANGE_COLOR_OF_TEACHER");
        }
        else if(configIdentifier == ConfigIdentifier.CHANGE_COLOR_OF_STUDENT){
            return prop.getProperty("CHANGE_COLOR_OF_STUDENT");
        }
        else if (configIdentifier == ConfigIdentifier.getStudentLogin){
            return prop.getProperty("getStudentLogin");
        }
        else if (configIdentifier == ConfigIdentifier.getTeacherLogin){
            return prop.getProperty("getTeacherLogin");
        }
        else if (configIdentifier == ConfigIdentifier.getLastNameHelpTeacherFromId){
            return prop.getProperty("getLastNameHelpTeacherFromId");
        }
        else if (configIdentifier == ConfigIdentifier.getLastNameHelpTeacherFromLastName){
            return prop.getProperty("getLastNameHelpTeacherFromLastName");
        }
        else if (configIdentifier == ConfigIdentifier.getAllStudents){
            return prop.getProperty("getAllStudents");
        }
        else if (configIdentifier == ConfigIdentifier.getAllTeachers){
            return prop.getProperty("getAllTeachers");
        }
        else if (configIdentifier == ConfigIdentifier.getAllCourses){
            return prop.getProperty("getAllCourses");
        }
        else if (configIdentifier == ConfigIdentifier.ShowTemperoryScoresForEA){
            return prop.getProperty("ShowTemperoryScoresForEA");
        }
        else if (configIdentifier == ConfigIdentifier.CopyLinkedListCourse_TemperoryScoresForEA){
            return prop.getProperty("CopyLinkedListCourse_TemperoryScoresForEA");
        }
        else if (configIdentifier == ConfigIdentifier.ShowCoursesWithFilterForNameCourse_TemperoryScoresForEA){
            return prop.getProperty("ShowCoursesWithFilterForNameCourse_TemperoryScoresForEA");
        }
        else if (configIdentifier == ConfigIdentifier.ShowCoursesWithFilterForTeacherName_TemperoryScoresForEA){
            return prop.getProperty("ShowCoursesWithFilterForTeacherName_TemperoryScoresForEA");
        }
        else if (configIdentifier == ConfigIdentifier.ShowCourses){
            return prop.getProperty("ShowCourses");
        }
        else if (configIdentifier == ConfigIdentifier.ShowTeachers){
            return prop.getProperty("ShowTeachers");
        }
        else if (configIdentifier == ConfigIdentifier.GiveCoursesFromAStudent){
            return prop.getProperty("GiveCoursesFromAStudent");
        }
        else if (configIdentifier == ConfigIdentifier.GiveCoursesFromATeacher){
            return prop.getProperty("GiveCoursesFromATeacher");
        }
        else if (configIdentifier == ConfigIdentifier.returnAllHelpTeachersFromACollege){
            return prop.getProperty("returnAllHelpTeachersFromACollege");
        }
        else if (configIdentifier == ConfigIdentifier.CanSignUpForDuplicateUsername){
            return prop.getProperty("CanSignUpForDuplicateUsername");
        }
        else if (configIdentifier == ConfigIdentifier.addStudent){
            return prop.getProperty("addStudent");
        }
        else if (configIdentifier == ConfigIdentifier.addTeacher){
            return prop.getProperty("addTeacher");
        }
        else if (configIdentifier == ConfigIdentifier.addCourse){
            return prop.getProperty("addCourse");
        }
        else if (configIdentifier == ConfigIdentifier.FindTeacher){
            return prop.getProperty("FindTeacher");
        }
        else if (configIdentifier == ConfigIdentifier.FindTeacherLastNameFromId){
            return prop.getProperty("FindTeacherLastNameFromId");
        }
        else if (configIdentifier == ConfigIdentifier.FindTeacherFirstNameFromId){
            return prop.getProperty("FindTeacherFirstNameFromId");
        }
        else if (configIdentifier == ConfigIdentifier.FindTeacherCollegeFromId){
            return prop.getProperty("FindTeacherCollegeFromId");
        }
        else if (configIdentifier == ConfigIdentifier.isValidTeacher){
            return prop.getProperty("isValidTeacher");
        }
        else if (configIdentifier == ConfigIdentifier.FindCourse){
            return prop.getProperty("FindCourse");
        }
        else if (configIdentifier == ConfigIdentifier.FindACourseFormACollege){
            return prop.getProperty("FindACourseFormACollege");
        }
        else if (configIdentifier == ConfigIdentifier.AddNewCourseToTeacher){
            return prop.getProperty("AddNewCourseToTeacher");
        }
        else if (configIdentifier == ConfigIdentifier.StringToTeacher){
            return prop.getProperty("StringToTeacher");
        }
        else if (configIdentifier == ConfigIdentifier.SearchStudentByStudentNumberAndName){
            return prop.getProperty("SearchStudentByStudentNumberAndName");
        }
        else if (configIdentifier == ConfigIdentifier.getTeacherOfACourse){
            return prop.getProperty("getTeacherOfACourse");
        }
        else if (configIdentifier == ConfigIdentifier.GiveCoursesOfATeacher){
            return prop.getProperty("GiveCoursesOfATeacher");
        }
        else if (configIdentifier == ConfigIdentifier.GiveCoursesOfACollege){
            return prop.getProperty("GiveCoursesOfACollege");
        }
        else if (configIdentifier == ConfigIdentifier.GiveStudentsOfACoursesFromString){
            return prop.getProperty("GiveStudentsOfACoursesFromString");
        }
        else if (configIdentifier == ConfigIdentifier.GiveStudentsOfACoursesFromId){
            return prop.getProperty("GiveStudentsOfACoursesFromId");
        }
        else if (configIdentifier == ConfigIdentifier.FindStudentFromId){
            return prop.getProperty("FindStudentFromId");
        }
        else if (configIdentifier == ConfigIdentifier.FindIndexCourseFormAStudentFromCourseName){
            return prop.getProperty("FindIndexCourseFormAStudentFromCourseName");
        }
        else if (configIdentifier == ConfigIdentifier.RemoveCourseFromATeachersAndStudents){
            return prop.getProperty("RemoveCourseFromATeachersAndStudents");
        }
        else if (configIdentifier == ConfigIdentifier.ShowableCoursesOfStudentTemporaryScores){
            return prop.getProperty("ShowableCoursesOfStudentTemporaryScores");
        }
        else if (configIdentifier == ConfigIdentifier.FindIndexCourseFormAStudentFromId){
            return prop.getProperty("FindIndexCourseFormAStudentFromId");
        }
        else if (configIdentifier == ConfigIdentifier.GetScore){
            return prop.getProperty("GetScore");
        }
        else if (configIdentifier == ConfigIdentifier.FindStudentsWantsToReject){
            return prop.getProperty("FindStudentsWantsToReject");
        }
        else if (configIdentifier == ConfigIdentifier.FindStudentsWantsToMinor){
            return prop.getProperty("FindStudentsWantsToMinor");
        }
        else if (configIdentifier == ConfigIdentifier.RemoveStudent){
            return prop.getProperty("RemoveStudent");
        }
        else if (configIdentifier == ConfigIdentifier.ShowPastCoursesForAStudent){
            return prop.getProperty("ShowPastCoursesForAStudent");
        }
        else if (configIdentifier == ConfigIdentifier.ShowStudentsForATeacherEA){
            return prop.getProperty("ShowStudentsForATeacherEA");
        }
        else if (configIdentifier == ConfigIdentifier.getStatusACourseFromAStudent){
            return prop.getProperty("getStatusACourseFromAStudent");
        }
        else if (configIdentifier == ConfigIdentifier.getScoreACourseFromAStudent){
            return prop.getProperty("getScoreACourseFromAStudent");
        }
        else if (configIdentifier == ConfigIdentifier.getPassesCourseFromAStudent){
            return prop.getProperty("getPassesCourseFromAStudent");
        }
        else if (configIdentifier == ConfigIdentifier.FindStudentsWantsToRecome){
            return prop.getProperty("FindStudentsWantsToRecome");
        }
        else if (configIdentifier == ConfigIdentifier.getIndexRequest){
            return prop.getProperty("getIndexRequest");
        }
        else if (configIdentifier == ConfigIdentifier.getWeighOfCoursesPass){
            return prop.getProperty("getWeighOfCoursesPass");
        }
        else if (configIdentifier == ConfigIdentifier.getNumPassForACourse){
            return prop.getProperty("getNumPassForACourse");
        }
        else if (configIdentifier == ConfigIdentifier.AddAndRemoveACourseFormTeachers){
            return prop.getProperty("AddAndRemoveACourseFormTeachers");
        }
        else if (configIdentifier == ConfigIdentifier.RemoveACourseFormTeachers){
            return prop.getProperty("RemoveACourseFormTeachers");
        }
        else if (configIdentifier == ConfigIdentifier.changePasswordForAStudent){
            return prop.getProperty("changePasswordForAStudent");
        }
        else if (configIdentifier == ConfigIdentifier.changePasswordAForTeacher){
            return prop.getProperty("changePasswordAForTeacher");
        }
        else if (configIdentifier == ConfigIdentifier.changeLastLoginStudent){
            return prop.getProperty("changeLastLoginStudent");
        }
        else if (configIdentifier == ConfigIdentifier.changeLastLoginTeacher){
            return prop.getProperty("changeLastLoginTeacher");
        }
        else if (configIdentifier == ConfigIdentifier.changeEducationStatusOfAStudent){
            return prop.getProperty("changeEducationStatusOfAStudent");
        }
        else if (configIdentifier == ConfigIdentifier.changeProtestForAStudent){
            return prop.getProperty("changeProtestForAStudent");
        }
        else if (configIdentifier == ConfigIdentifier.editEmailForAStudent){
            return prop.getProperty("editEmailForAStudent");
        }
        else if (configIdentifier == ConfigIdentifier.editPhoneForAStudent){
            return prop.getProperty("editPhoneForAStudent");
        }
        else if (configIdentifier == ConfigIdentifier.setImageAddressOfAStudent){
            return prop.getProperty("setImageAddressOfAStudent");
        }
        else if (configIdentifier == ConfigIdentifier.addIdTeacherToStudentForRecome){
            return prop.getProperty("addIdTeacherToStudentForRecome");
        }
        else if (configIdentifier == ConfigIdentifier.changeRecomeForStudentBecauseShowed){
            return prop.getProperty("changeRecomeForStudentBecauseShowed");
        }
        else if (configIdentifier == ConfigIdentifier.changeRecomeForStudentBecauseShowedWithIdTeacherShouldAddToAccepted){
            return prop.getProperty("changeRecomeForStudentBecauseShowedWithIdTeacherShouldAddToAccepted");
        }
        else if (configIdentifier == ConfigIdentifier.setMinorCollegeForAStudent){
            return prop.getProperty("setMinorCollegeForAStudent");
        }
        else if (configIdentifier == ConfigIdentifier.setDormRequestForAStudent){
            return prop.getProperty("setDormRequestForAStudent");
        }
        else if (configIdentifier == ConfigIdentifier.changeMinorForStudentBecauseShowedAndRejected){
            return prop.getProperty("changeMinorForStudentBecauseShowedAndRejected");
        }
        else if (configIdentifier == ConfigIdentifier.submitRecomeForAStudent){
            return prop.getProperty("submitRecomeForAStudent");
        }
        else if (configIdentifier == ConfigIdentifier.setRoomNumberForATeacher){
            return prop.getProperty("setRoomNumberForATeacher");
        }
        else if (configIdentifier == ConfigIdentifier.removeATeacher){
            return prop.getProperty("removeATeacher");
        }
        else if (configIdentifier == ConfigIdentifier.changeStatusTeacher){
            return prop.getProperty("changeStatusTeacher");
        }
        else if (configIdentifier == ConfigIdentifier.editEmailForATeacher){
            return prop.getProperty("editEmailForATeacher");
        }
        else if (configIdentifier == ConfigIdentifier.editPhoneForATeacher){
            return prop.getProperty("editPhoneForATeacher");
        }
        else if (configIdentifier == ConfigIdentifier.setImageAddressOfATeacher){
            return prop.getProperty("setImageAddressOfATeacher");
        }
        else if (configIdentifier == ConfigIdentifier.setAnswerAndScoreForStudent){
            return prop.getProperty("setAnswerAndScoreForStudent");
        }
        else if (configIdentifier == ConfigIdentifier.changeMinorStatusFromEA){
            return prop.getProperty("changeMinorStatusFromEA");
        }
        else if (configIdentifier == ConfigIdentifier.setTimesLoginForAStudent){
            return prop.getProperty("setTimesLoginForAStudent");
        }
        else if (configIdentifier == ConfigIdentifier.setTimesLoginForATeacher){
            return prop.getProperty("setTimesLoginForATeacher");
        }
        else if (configIdentifier == ConfigIdentifier.getProtestForCourseStudent){
            return prop.getProperty("getProtestForCourseStudent");
        }
        else if (configIdentifier == ConfigIdentifier.getAnswerForCourseStudent){
            return prop.getProperty("getAnswerForCourseStudent");
        }
        else if (configIdentifier == ConfigIdentifier.getScoreForCourseStudent){
            return prop.getProperty("getScoreForCourseStudent");
        }
        else if (configIdentifier == ConfigIdentifier.getMinorStatus){
            return prop.getProperty("getMinorStatus");
        }
        else if (configIdentifier == ConfigIdentifier.getRequestForRecommendation){
            return prop.getProperty("getRequestForRecommendation");
        }
        else if (configIdentifier == ConfigIdentifier.getIdTeacherForRequest){
            return prop.getProperty("getIdTeacherForRequest");
        }
        else if (configIdentifier == ConfigIdentifier.testConnection){
            return prop.getProperty("testConnection");
        }
        else if (configIdentifier == ConfigIdentifier.getThesisDefenceFromAStudent){
            return prop.getProperty("getThesisDefenceFromAStudent");
        }
        else if (configIdentifier == ConfigIdentifier.validStudentNumberForTA){
            return prop.getProperty("validStudentNumberForTA");
        }
        else if (configIdentifier == ConfigIdentifier.validDuplicatePublicIdForCourse){
            return prop.getProperty("validDuplicatePublicIdForCourse");
        }
        else if (configIdentifier == ConfigIdentifier.validPublicIdForCourse){
            return prop.getProperty("validPublicIdForCourse");
        }
        else if (configIdentifier == ConfigIdentifier.setExamDayForACourse){
            return prop.getProperty("setExamDayForACourse");
        }
        else if (configIdentifier == ConfigIdentifier.getAllCoursesFromACollege){
            return prop.getProperty("getAllCoursesFromACollege");
        }
        else if (configIdentifier == ConfigIdentifier.sendFileToServer){
            return prop.getProperty("sendFileToServer");
        }
        else if (configIdentifier == ConfigIdentifier.sendFileFromServer){
            return prop.getProperty("sendFileFromServer");
        }
        else if (configIdentifier == ConfigIdentifier.removeAFileFromServerForCw){
            return prop.getProperty("removeAFileFromServerForCw");
        }
        else if (configIdentifier == ConfigIdentifier.addNewFileInCwForExamOrHw){
            return prop.getProperty("addNewFileInCwForExamOrHw");
        }
        else if (configIdentifier == ConfigIdentifier.getAllTeacherFormACollege){
            return prop.getProperty("getAllTeacherFormACollege");
        }
        else if (configIdentifier == ConfigIdentifier.getAllCoursesFromACollegeForSelectUnit){
            return prop.getProperty("getAllCoursesFromACollegeForSelectUnit");
        }
        else if (configIdentifier == ConfigIdentifier.saveMessagesBecauseNotConnected){
            return prop.getProperty("saveMessagesBecauseNotConnected");
        }
        else if (configIdentifier == ConfigIdentifier.saveMessage){
            return prop.getProperty("saveMessage");
        }
        else if (configIdentifier == ConfigIdentifier.getChatRoomsInfo){
            return prop.getProperty("getChatRoomsInfo");
        }
        else if (configIdentifier == ConfigIdentifier.changeColorForMohseni){
            return prop.getProperty("changeColorForMohseni");
        }
        else if (configIdentifier == ConfigIdentifier.changeColorForAdmin){
            return prop.getProperty("changeColorForAdmin");
        }
        else if (configIdentifier == ConfigIdentifier.getMohseniLogin){
            return prop.getProperty("getMohseniLogin");
        }
        else if (configIdentifier == ConfigIdentifier.getAdminLogin){
            return prop.getProperty("getAdminLogin");
        }
        else if (configIdentifier == ConfigIdentifier.ShowStudentsForMohseni){
            return prop.getProperty("ShowStudentsForMohseni");
        }
        else if (configIdentifier == ConfigIdentifier.GiveCoursesOfAStudent){
            return prop.getProperty("GiveCoursesOfAStudent");
        }
        else if (configIdentifier == ConfigIdentifier.getFileNameOfFromUsername){
            return prop.getProperty("getFileNameOfFromUsername");
        }
        else if (configIdentifier == ConfigIdentifier.getFileNameOfFromStudentNumber){
            return prop.getProperty("getFileNameOfFromStudentNumber");
        }
        else if (configIdentifier == ConfigIdentifier.getYearsToUniversityForEA){
            return prop.getProperty("getYearsToUniversityForEA");
        }
        else if (configIdentifier == ConfigIdentifier.saveTimeForSelectionUnit){
            return prop.getProperty("saveTimeForSelectionUnit");
        }
        else if (configIdentifier == ConfigIdentifier.getLikedCourses){
            return prop.getProperty("getLikedCourses");
        }
        else if (configIdentifier == ConfigIdentifier.getSuggestedCourses){
            return prop.getProperty("getSuggestedCourses");
        }
        else if (configIdentifier == ConfigIdentifier.getStatusOfACourseForUnitSelection){
            return prop.getProperty("getStatusOfACourseForUnitSelection");
        }
        else if (configIdentifier == ConfigIdentifier.addACourseToLikedCourses){
            return prop.getProperty("addACourseToLikedCourses");
        }
        else if (configIdentifier == ConfigIdentifier.removeACourseFromLikedCourses){
            return prop.getProperty("removeACourseFromLikedCourses");
        }
        else if (configIdentifier == ConfigIdentifier.removeACourseFromLikedCourses){
            return prop.getProperty("removeACourseFromLikedCourses");
        }
        else if (configIdentifier == ConfigIdentifier.addACourseFromCoursesWantToGetThisTerm){
            return prop.getProperty("addACourseFromCoursesWantToGetThisTerm");
        }
        else if (configIdentifier == ConfigIdentifier.changeGroupForSelectionUnit){
            return prop.getProperty("changeGroupForSelectionUnit");
        }
        else if (configIdentifier == ConfigIdentifier.removeACourseFromCoursesWantToGetThisTerm){
            return prop.getProperty("removeACourseFromCoursesWantToGetThisTerm");
        }
        else if (configIdentifier == ConfigIdentifier.getAllStudentsFromOneCollegeAndFromOneYear){
            return prop.getProperty("getAllStudentsFromOneCollegeAndFromOneYear");
        }
        else if (configIdentifier == ConfigIdentifier.getAllMessagesFromAPerson){
            return prop.getProperty("getAllMessagesFromAPerson");
        }
        else if (configIdentifier == ConfigIdentifier.getAllMessagesFromTwoPersons){
            return prop.getProperty("getAllMessagesFromTwoPersons");
        }
        else if (configIdentifier == ConfigIdentifier.getUserNameFromName){
            return prop.getProperty("getUserNameFromName");
        }
        else if (configIdentifier == ConfigIdentifier.getEducationalMaterialsFromACourse){
            return prop.getProperty("getEducationalMaterialsFromACourse");
        }
        else if (configIdentifier == ConfigIdentifier.getExamsFromACourse){
            return prop.getProperty("getExamsFromACourse");
        }
        else if (configIdentifier == ConfigIdentifier.getHomeWorksFromACourse){
            return prop.getProperty("getHomeWorksFromACourse");
        }
        else if (configIdentifier == ConfigIdentifier.changeAnswerForHomeWorkOrExamForAStudent){
            return prop.getProperty("changeAnswerForHomeWorkOrExamForAStudent");
        }
        else if (configIdentifier == ConfigIdentifier.getMessagesForAStudent){
            return prop.getProperty("getMessagesForAStudent");
        }
        else if (configIdentifier == ConfigIdentifier.getMessagesForATeacher){
            return prop.getProperty("getMessagesForATeacher");
        }
        else if (configIdentifier == ConfigIdentifier.removeAMessageFromAStudent){
            return prop.getProperty("removeAMessageFromAStudent");
        }
        else if (configIdentifier == ConfigIdentifier.removeAMessageFromATeacherForMessages){
            return prop.getProperty("removeAMessageFromATeacherForMessages");
        }
        else if (configIdentifier == ConfigIdentifier.sendMessageToEAToGetACourse){
            return prop.getProperty("sendMessageToEAToGetACourse");
        }
        else if (configIdentifier == ConfigIdentifier.getTimeSelectUnitForAStudent){
            return prop.getProperty("getTimeSelectUnitForAStudent");
        }
        else if (configIdentifier == ConfigIdentifier.FindATeacherOrStudentForChatRoom){
            return prop.getProperty("FindATeacherOrStudentForChatRoom");
        }
        else if (configIdentifier == ConfigIdentifier.sendMessageToAPersonForRequestChatRoom){
            return prop.getProperty("sendMessageToAPersonForRequestChatRoom");
        }
        else if (configIdentifier == ConfigIdentifier.addStudentToCourseInCW){
            return prop.getProperty("addStudentToCourseInCW");
        }
        else if (configIdentifier == ConfigIdentifier.isAStudentTAOfACourse){
            return prop.getProperty("isAStudentTAOfACourse");
        }
        else if (configIdentifier == ConfigIdentifier.getNameOfAStudentFromId){
            return prop.getProperty("getNameOfAStudentFromId");
        }
        else if (configIdentifier == ConfigIdentifier.getStudentsNumberOfAStudentFromId){
            return prop.getProperty("getStudentsNumberOfAStudentFromId");
        }
        else if (configIdentifier == ConfigIdentifier.saveExamsOrHWsForCw){
            return prop.getProperty("saveExamsOrHWsForCw");
        }
        else if (configIdentifier == ConfigIdentifier.changeEducationalMaterialForACourse){
            return prop.getProperty("changeEducationalMaterialForACourse");
        }
        else if (configIdentifier == ConfigIdentifier.getCoursesOfATeacher){
            return prop.getProperty("getCoursesOfATeacher");
        }
        else if (configIdentifier == ConfigIdentifier.getCoursesGroup){
            return prop.getProperty("getCoursesGroup");
        }
        else if (configIdentifier == ConfigIdentifier.getStudentsWithFilterForMohseni){
            return prop.getProperty("getStudentsWithFilterForMohseni");
        }
        else if (configIdentifier == ConfigIdentifier.findStudentFromStudentNumber){
            return prop.getProperty("findStudentFromStudentNumber");
        }
        else if (configIdentifier == ConfigIdentifier.getAllStudentForATeacherInChatRoom){
            return prop.getProperty("getAllStudentForATeacherInChatRoom");
        }
        else if (configIdentifier == ConfigIdentifier.getAllStudentForABossAndEAInChatRoom){
            return prop.getProperty("getAllStudentForABossAndEAInChatRoom");
        }

        else if (configIdentifier == ConfigIdentifier.finishTimeSelection){
            return prop.getProperty("finishTimeSelection");
        }
        else if (configIdentifier == ConfigIdentifier.removeACourse){
            return prop.getProperty("removeACourse");
        }



        else if (configIdentifier == ConfigIdentifier.publicURL){
            return prop.getProperty("publicURL");
        }
        else if (configIdentifier == ConfigIdentifier.publicURLForServer){
            return prop.getProperty("publicURLForServer");
        }
        else if (configIdentifier == ConfigIdentifier.publicURLForUser){
            return prop.getProperty("publicURLForUser");
        }
        else{
            return null;
        }

    }

}
