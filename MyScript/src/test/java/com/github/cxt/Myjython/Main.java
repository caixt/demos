package com.github.cxt.Myjython;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.python.core.Py;
import org.python.core.PyBoolean;
import org.python.core.PyException;
import org.python.core.PyObject;
import org.python.core.PyStringMap;
import org.python.core.PySystemState;
import org.python.core.PyType;
import org.python.util.PythonInterpreter;


public class Main {
	
	private static final String TRUE = "true";
	private static final String FALSE = "false";

	private PythonInterpreter interpreter;
	
	@Before
	public void before(){
		System.setProperty(PySystemState.PYTHON_CONSOLE_ENCODING, "utf-8");
	}

	@Test
	public void test1() throws FileNotFoundException, URISyntaxException {
		interpreter = new PythonInterpreter();
		interpreter.setLocals(new PyStringMap());
		Map<String, Serializable> params = new HashMap<>();
		params.put("x", "abcd");
		for (Map.Entry<String, Serializable> entry : params.entrySet()) {
			interpreter.set(entry.getKey(), entry.getValue());
		}
		File file = new File(this.getClass().getResource("testPython.py").toURI());
		interpreter.execfile(new FileInputStream(file));
		Iterator<PyObject> localsIterator = interpreter.getLocals().asIterable().iterator();
		Map<String, Serializable> returnValue = new HashMap<>();
		while (localsIterator.hasNext()) {
			String key = localsIterator.next().asString();
			PyObject value = interpreter.get(key);
			Serializable javaValue = resolveJythonObjectToJavaExec(value, key);
			returnValue.put(key, javaValue);
		}
		System.out.println("!");
	}
	
	@Test
	public void test2() throws FileNotFoundException, URISyntaxException {
		interpreter = new PythonInterpreter();
		String script = "true";
		Map<String, Serializable> context  = new HashMap<>();
		context.put("url", "abcd");
		cleanInterpreter(interpreter);
		prepareInterpreterContext(context);
		Serializable value = eval(interpreter, script);
		System.out.println(value);
	}
	
	
	private void cleanInterpreter(PythonInterpreter interpreter) {
        interpreter.setLocals(new PyStringMap());
    }
	
	private Serializable eval(PythonInterpreter interpreter, String script) {
		PyObject evalResultAsPyObject = interpreter.eval(script);
		Serializable evalResult;
		evalResult = resolveJythonObjectToJavaEval(evalResultAsPyObject, script);
		return evalResult;
	}

    private void prepareInterpreterContext(Map<String, ? extends Serializable> context) {
        for (Map.Entry<String, ? extends Serializable> entry : context.entrySet()) {
            interpreter.set(entry.getKey(), entry.getValue());
        }
        if (interpreter.get(TRUE) == null)
            interpreter.set(TRUE, Boolean.TRUE);
        if (interpreter.get(FALSE) == null)
            interpreter.set(FALSE, Boolean.FALSE);
    }

	private Serializable resolveJythonObjectToJavaExec(PyObject value, String key) {
		String errorMessage = "Non-serializable values are not allowed in the output context of a Python script:\n"
				+ "\tConversion failed for '" + key + "' (" + String.valueOf(value) + "),\n"
				+ "\tThe error can be solved by removing the variable from the context in the script: e.g. 'del " + key
				+ "'.\n";
		return resolveJythonObjectToJava(value, errorMessage);
	}

	private Serializable resolveJythonObjectToJavaEval(PyObject value, String expression) {
		String errorMessage = "Evaluation result for a Python expression should be serializable:\n"
				+ "\tConversion failed for '" + expression + "' (" + String.valueOf(value) + ").\n";
		return resolveJythonObjectToJava(value, errorMessage);
	}

	private Serializable resolveJythonObjectToJava(PyObject value, String errorMessage) {
		if (value == null) {
			return null;
		}
		if (value instanceof PyBoolean) {
			PyBoolean pyBoolean = (PyBoolean) value;
			return pyBoolean.getBooleanValue();
		}
		try {
			return Py.tojava(value, Serializable.class);
		} catch (PyException e) {
			PyObject typeObject = e.type;
			if (typeObject instanceof PyType) {
				PyType type = (PyType) typeObject;
				String typeName = type.getName();
				if ("TypeError".equals(typeName)) {
					throw new RuntimeException(errorMessage, e);
				}
			}
			throw e;
		}
	}
}
