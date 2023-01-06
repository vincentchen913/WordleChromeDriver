public class Word implements Comparable<Word>{
		public String name;
		private int[] inventory;
		private int val;
		
		static int[] scores = new int[26];
		
		public static void fillScores() {
			scores[0] = 405; //a
			scores[1] = 115;
			scores[2] = 157;
			scores[3] = 181;
			scores[4] = 448; //e
			scores[5] = 79;
			scores[6] = 118;
			scores[7] = 133;
			scores[8] = 281; //i
			scores[9] = 21;
			scores[10] = 102;
			scores[11] = 250;
			scores[12] = 142;
			scores[13] = 214;
			scores[14] = 295; //o
			scores[15] = 146;
			scores[16] = 9;
			scores[17] = 309;
			scores[18] = 461;
			scores[19] = 240;
			scores[20] = 186; //u
			scores[21] = 52;
			scores[22] = 77;
			scores[23] = 24;
			scores[24] = 154; //y
			scores[25] = 25;
		}
		
		public Word(String name) {
			this.name = name;
			inventory = new int[26];
			for(int i = 0; i < name.length(); i++) {
				char c = name.charAt(i);
				int index = (int)c - (int)'a';
				inventory[index]++;
			}
			for(int i = 0; i < 26; i++) {
				if(inventory[i] > 0) {
					val += scores[i];
				}
			}
		}
		
		public int indexOf(char c) {
			// TODO Auto-generated method stub
			return 0;
		}

		public int compareTo(Word other) {
			return this.val - other.val;
		}
		
		public String getName() {
			return name;
		}
		
		public int getVal() {
			return val;
		}
		
		public boolean containsDuplicates() {
			for(int i = 0; i < 26; i++) {
				if(inventory[i] > 1) {
					return false;
				}
			}
			return true;
		}
	}
