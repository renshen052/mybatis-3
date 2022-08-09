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

import org.apache.ibatis.autoconstructor.PrimitiveSubject;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
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
     * 用于调试 forEach 相关的断点
     */
    @Test
    public void forEachTest() {
        final SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            final SqlNodeMapper mapper = sqlSession.getMapper(SqlNodeMapper.class);
            ArrayList<Integer> idList = new ArrayList<>();
            idList.add(1);
            idList.add(2);
            List<MyPrimitiveSubject> subjectList = mapper.getSubject(idList);
            System.out.printf("查询结果\n" + subjectList.toString());
        } finally {
            sqlSession.close();
        }
    }
}
