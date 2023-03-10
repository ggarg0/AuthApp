/*
 * *
 *  * The MIT License (MIT)
 *  * <p>
 *  * Copyright (c) 2022
 *  * <p>
 *  * Permission is hereby granted, free of charge, to any person obtaining a copy
 *  * of this software and associated documentation files (the "Software"), to deal
 *  * in the Software without restriction, including without limitation the rights
 *  * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  * copies of the Software, and to permit persons to whom the Software is
 *  * furnished to do so, subject to the following conditions:
 *  * <p>
 *  * The above copyright notice and this permission notice shall be included in all
 *  * copies or substantial portions of the Software.
 *  * <p>
 *  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  * SOFTWARE.
 *
 */
               
INSERT INTO USERS (firstname, lastname, username, password, team, role, approved, active)
VALUES ('Adam', 'Sandler', 'adam@gmail.com', '$2a$10$uGibDOMvRoRYggPgRTvOX.dtqqb7sED2V5KJSuNEh6KbKMUcv7uka', 'Team A', 'Admin', 'Yes', 'Yes'),
	   ('Brad', 'Pitt', 'brad@gmail.com', '$2a$10$qFG8mxRGv1.xlUD19LvgceJqIsxqkfOPdt7c432yeeGzogTnFjt7i', 'Team B', 'Developer', 'Yes', 'Yes' ),
	   ('Allan', 'Waugh', 'allan@gmail.com', '$2a$10$4lYJ8iOFJl3VVJOR.ivqVesOsfwXDWKWavGNe9V4gOMVqBuIiBfkS', 'Team A', 'Developer', 'No', 'Yes'),
	   ('Chris', 'Dale', 'chris@gmail.com', '$2a$10$NI0EmLmkTqE9QbCzGXCtCu3Zg8iMIwrFL4eMYV58WkLD7fuNUGxNW', 'Team B', 'Developer', 'Yes', 'No' );
	   
INSERT INTO LOADDATA (datatypeid, datatype, datavalue)
VALUES  (1, 'Role', 'Developer'),
		(2, 'Role', 'Admin'),
		(3, 'Team', 'Team A'),
		(4, 'Team', 'Team B'),
		(5, 'Team', 'Team C'),
		(6, 'Team', 'Team D');

