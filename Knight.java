
public class Knight {
	
	private int id;
	
	private Agenda agendaNew;
	
	private Agenda agendaComplete;
	
	private Quest currentQuest;

//	private Hall greatHall;
	
	private Knight(int id, Agenda agendaNew, Agenda agendaComplete, Hall greatHall) {
		this.id = id;
		this.agendaNew = agendaNew;
		this.agendaComplete = agendaComplete;
//		this.greatHall = greatHall;
		this.currentQuest = null;
	}
	
	public void addQuest(Quest quest) {
		currentQuest = quest;
	}
	
}
