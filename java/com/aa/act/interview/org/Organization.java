package com.aa.act.interview.org;

import java.util.Collection;
import java.util.Optional;
import java.util.Stack;

public abstract class Organization {

	private Position root;
	private int Id = 1234; 
	
	public Organization() {
		root = createOrganization();
	}
	
	protected abstract Position createOrganization();
	
	/**
	 * hire the given person as an employee in the position that has that title
	 * 
	 * @param person
	 * @param title
	 * @return the newly filled position or empty if no position has that title
	 */
	public Optional<Position> hire(Name person, String title) {
		Stack<Position> stack = new Stack<>();
		stack.push(root);   // push the first part of stack which consists of CEO that ranks above the other titles
		while(!stack.isEmpty())
		{
			Position temp = stack.pop();             // pop it out to get access to either the current level or lower titles below it. 
			if(temp.getTitle() == title && temp.isFilled() == false)                       // redundant code due to the fact that CEO will not be assigned if it was not written here
			{
				Optional<Employee> newHire = Optional.ofNullable(new Employee(Id++, person));
				temp.setEmployee(newHire);
				return Optional.ofNullable(temp); 
			}
			Collection<Position> directReports = temp.getDirectReports(); 
			if(directReports != null)
			{
				for(Position p: directReports)          // iterate through titles until we encounter the titles we want to fill and ensure that it is not taken yet. 
				{
					if(p.getTitle() == title && p.isFilled() == false)
					{
						Optional<Employee> newHire = Optional.ofNullable(new Employee(Id++, person));
						p.setEmployee(newHire);
						return Optional.ofNullable(p); 
					}
					stack.push(p);   // push every title level we encounter to ensure we iterate throughout the entire list. 
				}
			}
		}
		return Optional.empty();
	}

	@Override
	public String toString() {
		return printOrganization(root, "");
	}
	
	private String printOrganization(Position pos, String prefix) {
		StringBuffer sb = new StringBuffer(prefix + "+-" + pos.toString() + "\n");
		for(Position p : pos.getDirectReports()) {
			sb.append(printOrganization(p, prefix + "\t"));
		}
		return sb.toString();
	}
}
