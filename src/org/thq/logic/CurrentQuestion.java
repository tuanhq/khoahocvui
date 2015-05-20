/**
 * 
 */
package org.thq.logic;

import org.thq.business.QuestionDTO;

/**
 * @author tuanhq
 *
 */
public class CurrentQuestion {
	private static QuestionDTO questionDto;
	private static boolean lockCurrentQuestion  = true;
	private CurrentQuestion(){
		
	}
	public static synchronized QuestionDTO getCurrentQuestion() throws InterruptedException{
//		if(!lockCurrentQuestion){
//			return questionDto;
//		}else{
//			wait();
//			return questionDto;
//		}
		return questionDto;
	}
	public static synchronized void setCurrentQuestion(){
		
		if(!lockCurrentQuestion){
			lockCurrentQuestion = true;
		}
		//get current database va set cho dto
		
		
		lockCurrentQuestion = false;
		
	}
	

}
