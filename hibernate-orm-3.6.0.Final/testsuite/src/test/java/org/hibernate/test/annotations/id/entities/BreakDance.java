//$Id$
package org.hibernate.test.annotations.id.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.TableGenerator;
import javax.persistence.GenerationType;

/**
 * @author Emmanuel Bernard
 */
@Entity
public class BreakDance {
	@Id
	@GeneratedValue(generator = "memencoIdGen", strategy = GenerationType.TABLE)
	@TableGenerator(
		name = "memencoIdGen",
		table = "hi_id_key",
		pkColumnName = "id_key",
		valueColumnName = "next_hi",
		pkColumnValue = "issue",
		allocationSize = 1
	)
	public Integer id;
	public String name;
}
