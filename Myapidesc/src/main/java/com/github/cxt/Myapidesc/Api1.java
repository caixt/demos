package com.github.cxt.Myapidesc;

public interface Api1 {
	/**
	 * @api {POST} api1/test1 测试1
	 * @apiGroup api1
	 * @apiName 测试1
	 * @apiDescription 描述1
	 * @apiVersion 1.0.0
	 * @apiParam (MyGroup) {Integer} id Users unique ID.
	 * @apiParam (MyGroup) {String} [key] Users searchKey
	 * @apiParam  {Integer} [level=0] level..
	 * @apiParamExample {json} Request-Example:
	 *     {
	 *       "id": 4711
	 *     }
	 */
	public void test1();
	
	/**
	 * @api {POST} api1/test2 测试2
	 * @apiGroup api1
	 * @apiName 测试2
	 * @apiDescription 描述2
	 * @apiVersion 1.0.0
	 * @apiParamExample {json} Request-Example:
	 *     {
	 *       "id": 4711
	 *     }
	 */
	public void test2();
	
	/**
	 * @api {POST} api1/test3 测试3
	 * @apiGroup api1
	 * @apiName 测试3
	 * @apiDescription 描述3
	 * @apiVersion 2.0.0
	 * @apiHeader {String} access-key Users unique access-key.
	 * @apiHeaderExample {json} Header-Example:
	 *     {
	 *       "Accept-Encoding": "Accept-Encoding: gzip, deflate"
	 *     }
	 * @apiParam  {String} json body
	 * @apiParamExample {json} Request-Example:
	 *     {
	 *       "id": 4711
	 *     }
	 */
	public void test3();
	
	/**
	 * @api {POST} api1/test3 测试4
	 * @apiGroup api1
	 * @apiIgnore Not finished Method
	 */
	public void test4();
	
}
