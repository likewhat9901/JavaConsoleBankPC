package banking.threeby3;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class PuzzleMain {
	static Scanner scan = new Scanner(System.in);
	static int t1 = 2;
	static int t2 = 2;
	
	
	public static char[][] shuffle(char[][] arr) {
		
		Random rand = new Random();
		
		int randomNum = 0;
		int outArrNum = 0;
		int i;
		int shuffleNum = 100;
		
		//게임 진행
		for(i = 0 ; i < shuffleNum ; ) {

			char temp = arr[t1][t2];
			randomNum = rand.nextInt(4)+1;
				
			try {
				switch (randomNum) {
				case 1:
					arr[t1][t2] = arr[t1][++t2];
					arr[t1][t2] = temp;
					break;
				case 2:
					arr[t1][t2] = arr[t1][--t2];
					arr[t1][t2] = temp;
					break;
				case 3:
					arr[t1][t2] = arr[++t1][t2];
					arr[t1][t2] = temp;
					break;
				case 4:
					arr[t1][t2] = arr[--t1][t2];
					arr[t1][t2] = temp;
					break;
				default:
					continue;
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				outArrNum++;
				switch (randomNum) {
				case 1:
					t2--;
					break;
				case 2:
					t2++;
					break;
				case 3:
					t1--;
					break;
				case 4:
					t1++;
					break;
				default:
					continue;
				}
			}
			i++;
		}
		System.out.println("벗어난 횟수: " + outArrNum);
		
		return arr;
	}
	
	public static void main(String[] args) {
		
		//2차원 배열 선언
		char[][] arr = {
			    {'1', '2', '3'},
			    {'4', '5', '6'},
			    {'7', '8', 'X'}
		};
		
		//정답 배열
		char[][] answer = {
			    {'1', '2', '3'},
			    {'4', '5', '6'},
			    {'7', '8', 'X'}
		};
		
		arr = shuffle(arr);
		
		//게임 진행
		while(true) {
			//2차원 배열 출력
			System.out.println("=======");
			for(char[] row : arr) {
				for(char num : row) {
					System.out.print(num + "  ");
				}
				System.out.println();
			}
			System.out.println("=======");
			
			System.out.println("[이동] a: Left | d: Right | w: Up | s: Down");
			System.out.println("[종료] x: Exit");
			System.out.print("키를 입력해주세요: ");
			String move = scan.nextLine();
			
			char temp = arr[t1][t2];
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
				case "x":
					System.out.println("프로그램을 종료합니다.");
					System.exit(0);
					break;
				default:
					System.out.println("====잘못 입력했습니다====");
					continue;
				}
				
			} catch (ArrayIndexOutOfBoundsException e) {
				System.out.println("xxxxxxxxxxxxxx");
				System.out.println("xxxx이동불가xxxx");
				System.out.println("xxxxxxxxxxxxxx");
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
					return;
				}
				continue;
			}
			
			boolean isEqual = Arrays.deepEquals(arr, answer);
			
			if (isEqual) {
				System.out.println("==^^정답입니다^^==");
				System.out.println("=======");
				for(char[] row : arr) {
					for(char num : row) {
						System.out.print(num + "  ");
					}
					System.out.println();
				}
				System.out.println("=======");
				
				System.out.print("재시작하시겠습니까?(y 누르면 재시작, 나머지는 종료): ");
				String restart = scan.nextLine();
				
				switch (restart) {
				case "y":
					t1 = 2;
					t2 = 2;
					shuffle(arr);
					continue;
				default:
					System.out.println("프로그램을 종료합니다.");
					System.exit(0);
					break;
				}
			}
		}
	}
}
