package org.test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class testMain {
    public static void main(String[] args) throws IllegalAccessException, InstantiationException {
        People people = new People();

        people.doIt("I'm a student!");
    }
}

interface Say {
    void say(String word);
}

class Student implements Say {
    public void print(String say) {
        System.out.println(say);
    }

    @Override
    public void say(String word) {
        print(word);
    }
}

interface ExecutorCallbackHandler<M, P> {
    void execute(M mapper, P param);
}


class DoSomeThing {

    /*public <M, P> void batchDo(ExecutorCallbackHandler<M, P> executorCallbackHandler, P param) throws IllegalAccessException, InstantiationException {
        Type[] genTypes = executorCallbackHandler.getClass().getGenericInterfaces();
        Type[] params = ((ParameterizedType) genTypes[0]).getActualTypeArguments();
        Class<M> mapperClass = (Class<M>) params[0];
        M mapper = mapperClass.newInstance();
        executorCallbackHandler.execute(mapper, param);
    }*/
    public <M, P> void batchDo(ExecutorCallbackHandler<M, P> executorCallbackHandler, P param, Class<M> clazz) throws IllegalAccessException, InstantiationException {
    	M mapper = (M) clazz.newInstance();
    	executorCallbackHandler.execute(mapper, param);
    }
}

class AbstractPeople<M extends Say> {

    private DoSomeThing doSomeThing = new DoSomeThing();
    private Class<M> clazz;
    public AbstractPeople(Class<M> clazz) {
    	this.clazz = clazz;
    }

    public void doIt(String param) throws InstantiationException, IllegalAccessException {
        doSomeThing.batchDo(new ExecutorCallbackHandler<M, String>() {
            @Override
            public void execute(M people, String param) {
                people.say(param);
            }
        }, param, clazz);
    }
}

class People extends AbstractPeople<Student> {

	public People() {
		super(Student.class);
	}
}
