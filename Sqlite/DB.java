package com.e.capstone;

/*定义 数据库所有的表和字段 */
public class DB {

    public static final class NurseTB{
        public static final String Name = "NurseTB";

        public static final class Cols{
            public static final String UUID = "uuid";
            public static final String UNAME = "uname";
            public static final String PWD = "pwd";
            public static final String NAME = "name";
            public static final String ADDR = "addr";
            public static final String PHONE = "phone";
            public static final String WTIME = "wtime";       //工作时段，可能需要修改下类型
        }
    }
    public static final class DocTable{
        public static final String Name = "DocTable";

        public static final class Cols{
            public static final String UUID = "uuid";
            public static final String UNAME = "uname";
            public static final String PWD = "pwd";
            public static final String NAME = "name";
            public static final String DEP = "dep";
            public static final String PHONE = "phone";
            public static final String TITLE = "title";
        }
    }

    public static final class PatientTB{
        public static final String Name = "PatientTB";

        public static final class Cols{

            //public static final String UUID = "uuid";
            public static final String NAME = "name";
            public static final String AGE = "age";
            public static final String SEX = "sex";
            public static final String ADDR = "addr";
            public static final String PID = "pid";       //病人ID，
           // public static final String BID = "bid";       //病人床号，
            public static final String DID = "did";         //负责病人的医生ID
            public static final String DESC= "descrip";        //患病详情
        }
    }
    public static final class SibTB{
        public static final String Name = "SibTB";

        public static final class Cols{
            public static final String PWD = "pwd";
            public static final String UNAME = "uname";
            public static final String UUID = "uuid";
            public static final String NAME = "name";
            public static final String PHONE="phone";       //联系方式
            public static final String ADDR = "addr";       //住址
            public static final String PID = "pid";       //病人ID，关联他的病人
        }
    }

    public static final class JinfoTB{
        public static final String Name = "JinfoTB";

        public static final class Cols{
            public static final String UUID = "uuid";       //job id
            public static final String PID = "pid";
            //public static final String NID = "nid";
            public static final String DID = "did";
            public static final String WID = "workID";
           //public static final String SID  = "sid";
            public static final String TIME = "time";       //工作时间安排，需要修改类型
            public static final String DESCP = "descp";       //工作内容
        }
    }
}


