package banking.threeby3;

import java.util.Arrays;
import java.util.Scanner;

public class PuzzleMain {

	static Scanner scan = new Scanner(System.in);
	
	public static void main(String[] args) {
		
		//2차원 배열 선언
		char[][] arr = {
			    {'1', '2', '3'},
			    {'4', '5', '6'},
			    {'7', '8', ' '}
		};
		
		//정답
		char[][] answer = {
			    {'1', '2', '3'},
			    {'4', '5', '6'},
			    {'7', '8', ' '}
		};
		
		//빈공간 위치
		//t1: 행(가로) / t2: 열(세로)
		int t1 = 2;
		int t2 = 2;
		
		//게임 진행
		while(true) {
			System.out.println("=======");
			//행(가로줄)
			for(char[] row : arr) {
				//행 안의 숫자
				for(char num : row) {
					System.out.print(num + "  ");
				}
				System.out.println();
			}
			
			char temp = arr[t1][t2];
			String move = scan.nextLine();
				
			try {
				switch (move) {
				case "a":
					arr[t1][t2] = arr[t1][++t2];
					arr[t1][t2] = temp;
					break;
				case "d":
					arr[t1][t2] = arr[t1][--t2];
					arr[t1][t2] = temp;
					break;
				case "w":
					arr[t1][t2] = arr[++t1][t2];
					arr[t1][t2] = temp;
					break;
				case "s":
					arr[t1][t2] = arr[--t1][t2];
					arr[t1][t2] = temp;
					break;
				default:
					System.out.println("잘못 입력했습니다.");
					break;
				}
				
			} catch (ArrayIndexOutOfBoundsException e) {
				System.out.println("벗어남.");
				switch (move) {
				case "a":
					t2--;
					break;
				case "d":
					t2++;
					break;
				case "w":
					t1--;
					break;
				case "s":
					t1++;
					break;
				default:
					System.out.println("오류");
					break;
				}
				continue;
			}
			
			boolean isEqual = Arrays.deepEquals(arr, answer);
			
			if (isEqual) {
				System.out.println("정답입니다.");
				System.out.println("다시시작?");
				int restart = scan.nextInt();
				scan.nextLine();
				
				switch (restart) {
				case 1:
					continue;
				case 2:
					System.exit(0);
				default:
					break;
				}
			}
		}
		
	}

}
