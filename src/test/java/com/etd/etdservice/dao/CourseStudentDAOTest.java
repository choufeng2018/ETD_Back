package com.etd.etdservice.dao;

import com.etd.etdservice.bean.BaseResponse;
import com.etd.etdservice.bean.course.Course;
import com.etd.etdservice.bean.course.response.ResponseIsAttendCourse;
import com.etd.etdservice.bean.users.Student;
import com.etd.etdservice.bean.users.Teacher;
import com.etd.etdservice.serivce.CourseService;
import com.etd.etdservice.utils.DoubleUtil;
import com.etd.etdservice.utils.StringUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import java.util.Date;

import static org.junit.Assert.assertEquals;
@RunWith(SpringRunner.class)
@SpringBootTest
public class CourseStudentDAOTest {
    @Autowired
    private StudentDAO studentDAO;
    @Autowired
    private CourseDAO courseDAO;

    private  static TeacherDAO teacherDAO;
    @Autowired
    public void setTeacherDAO(TeacherDAO teacherDAO){
        this.teacherDAO = teacherDAO;
    }
    @Autowired
    private CourseService courseService;

    public static Course mockCourse() {
        Course course = new Course();
        Teacher teacher  = UserDAOTest.mockTeacher();
        teacherDAO.create(teacher);
        Teacher teacherRes = teacherDAO.queryByUserName(teacher.getUserName());
        course.setTeacherId(teacherRes.getId());
        course.setCourseNum(StringUtil.generateRandomString("courseNUm"));
        course.setAvatarUrl(StringUtil.generateRandomString("avatarUrl"));
        course.setCreateTime(new Date());
        course.setStartTime(new Date());
        course.setDescription(StringUtil.generateRandomString("description"));
        course.setName(StringUtil.generateRandomString("name"));
        course.setNote(StringUtil.generateRandomString("note"));
        course.setPages(StringUtil.generateRandomString("pages"));
        course.setScore(DoubleUtil.nextDouble(0, 5));
        return course;
    }

    //学生选课测试
    @Test
    public void attendCourse(){
        //mock一个学生并插入students表中
        Student student = UserDAOTest.mockStudent();
        studentDAO.create(student);
        String sessionKey = student.getSessionKey();
        //mock一门课并插入courses表中
        Course course = mockCourse();
        courseDAO.create(course);
        //获取courseId
        int courseId = courseDAO.queryByCourseNum(course.getCourseNum()).getId();

        BaseResponse baseResponse = courseService.attendCourse(courseId, sessionKey);
        assertEquals(baseResponse.isSuccess(),true);
        assertEquals("",baseResponse.getErrMsg());
    }

    //学生退课测试
    @Test
    public void withdrawCourse(){
        //mock一个学生并插入students表中
        Student student = UserDAOTest.mockStudent();
        studentDAO.create(student);
        String sessionKey = student.getSessionKey();
        //mock一门课并插入courses表中
        Course course = mockCourse();
        courseDAO.create(course);
        //获取courseId
        int courseId = courseDAO.queryByCourseNum(course.getCourseNum()).getId();
        //先插入选课表
        courseService.attendCourse(courseId, sessionKey);
        //再删除选课表
        BaseResponse baseResponse = courseService.withdrawCourse(courseId,sessionKey);
        assertEquals(baseResponse.isSuccess(),true);
        assertEquals("",baseResponse.getErrMsg());
    }

    //获取某学生是否参加了某门课程测试
    @Test
    public void isAttendCourse(){
        //mock一个学生并插入students表中
        Student student = UserDAOTest.mockStudent();
        studentDAO.create(student);
        String sessionKey = student.getSessionKey();
        //mock一门课并插入courses表中
        Course course = mockCourse();
        courseDAO.create(course);
        //获取courseId
        int courseId = courseDAO.queryByCourseNum(course.getCourseNum()).getId();
        //先插入选课表
        courseService.attendCourse(courseId, sessionKey);
        //再查询
        ResponseIsAttendCourse attendCourse = courseService.isAttendCourse(courseId,sessionKey);
        assertEquals(attendCourse.isSuccess(),true);
        assertEquals(attendCourse.isSuccess(),true);
        assertEquals(attendCourse.getErrMsg(),"");
    }
}
