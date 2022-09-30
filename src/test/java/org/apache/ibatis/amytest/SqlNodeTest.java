/**
 * Copyright 2009-2017 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.ibatis.amytest;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.Reader;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class SqlNodeTest {
    private static SqlSessionFactory sqlSessionFactory;

    @BeforeClass
    public static void setUp() throws Exception {
        // 创建 SqlSessionFactory
        final Reader reader = Resources.getResourceAsReader("org/apache/ibatis/amytest/mybatis-config.xml");
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        reader.close();

        // populate in-memory database
        final SqlSession session = sqlSessionFactory.openSession();
        final Connection conn = session.getConnection();
        final Reader dbReader = Resources.getResourceAsReader("org/apache/ibatis/amytest/CreateDB.sql");
        final ScriptRunner runner = new ScriptRunner(conn);
        runner.setLogWriter(null);
        runner.runScript(dbReader);
        conn.close();
        dbReader.close();
        session.close();
    }

    /**
     * forEach
     */
    @Test
    public void forEachTest() {
        final SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            final SqlNodeMapper mapper = sqlSession.getMapper(SqlNodeMapper.class);
            ArrayList<Integer> idList = new ArrayList<>();
            idList.add(3);
            idList.add(4);
            List<MyPrimitiveSubject> subjectList = mapper.getSubject(idList);
            System.out.printf("查询结果\n" + subjectList.toString());
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 查单个
     */
    @Test
    public void getSingleSubjectTest() {
        final SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            final SqlNodeMapper mapper = sqlSession.getMapper(SqlNodeMapper.class);

            MyPrimitiveSubject subject = mapper.getSingleSubject(1);
            System.out.printf("查询结果\n" + subject.toString());
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 插入
     */
    @Test
    public void insert() {
        final SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH,false);
        final SqlSession sqlSession2 = sqlSessionFactory.openSession();
        try {
            final SqlNodeMapper mapper = sqlSession.getMapper(SqlNodeMapper.class);

            List<MyPrimitiveSubject> myPrimitiveSubjectList = getMyPrimitiveSubject();
            for (MyPrimitiveSubject myPrimitiveSubject : myPrimitiveSubjectList) {
                mapper.insertSingleSubject(myPrimitiveSubject);
            }
            sqlSession.commit();



            //输出
            SqlNodeMapper mapper2 = sqlSession2.getMapper(SqlNodeMapper.class);
            ArrayList<Integer> idList = new ArrayList<>();
            idList.add(10);
            idList.add(11);
            List<MyPrimitiveSubject> subjectList = mapper2.getSubject(idList);
            System.out.println(subjectList);
        } finally {
            sqlSession.close();
            sqlSession2.close();
        }
    }

    private List<MyPrimitiveSubject> getMyPrimitiveSubject(){
        List<MyPrimitiveSubject> list = new ArrayList<>();
        MyPrimitiveSubject myPrimitiveSubject1 = new MyPrimitiveSubject();
        myPrimitiveSubject1.setId(10);
        myPrimitiveSubject1.setName("");
        myPrimitiveSubject1.setAge(0);
        myPrimitiveSubject1.setHeight(0);
        myPrimitiveSubject1.setWeight(0);


        MyPrimitiveSubject myPrimitiveSubject2 = new MyPrimitiveSubject();
        myPrimitiveSubject2.setId(11);
        myPrimitiveSubject2.setName("");
        myPrimitiveSubject2.setAge(0);
        myPrimitiveSubject2.setHeight(0);
        myPrimitiveSubject2.setWeight(0);

        list.add(myPrimitiveSubject1);
        list.add(myPrimitiveSubject2);
        return list;
    }
}
