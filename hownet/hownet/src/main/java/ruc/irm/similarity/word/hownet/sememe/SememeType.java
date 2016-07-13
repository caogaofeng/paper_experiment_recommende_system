package ruc.irm.similarity.word.hownet.sememe;

/**
 * 义原的类型定义<br/>
 * <ul>
 * <li>1：Event|事件</li>
 * <li>2：Entity|实体 </li>
 * <li>3:Attribute|属性 </li>
 * <li>4：Quantity|数量</li>
 * <li>5：aValue|属性值</li>
 * <li>6：qValue|数量值</li>
 * <li>7: Secondary Feature|第二特征</li>
 * <li>8: Syntax|语法</li>
 * <li>9: EventRole|动态角色</li>
 * <li>10:EventFeatures|动态属性</li>
 * <li>0：未知</li>
 * </ul>
 * 
 * 其中1~7为基本义元，8为语法义元，9、10为关系义元<br/>
 * 
 * @author <a href="mailto:iamxiatian@gmail.com">夏天</a>
 * @organization 中国人民大学信息资源管理学院 知识工程实验室
 * @deprecated
 */
public interface SememeType {	 
	  /** Event|事件类型定义 */
	  public static final int Event = 1;
	  
	  /** Entity|实体类型定义*/
	  public static final int Entity = 2;
	  
	  /** Attribute|属性类型定义*/
	  public static final int Attribute = 3;

	  /** Quantity|数量类型定义*/
	  public static final int Quantity = 4;

	  /** aValue|属性值类型定义*/
	  public static final int AValue = 5;

	  /** qValue|数量值类型定义*/
	  public static final int QValue = 6;
	  
	  /** Secondary Feature|第二特征类型定义*/
	  public static final int SecondaryFeature = 7;
	  
	  /** Syntax|语法类型定义*/
	  public static final int Syntax = 8;
	  
	  /** EventRole|动态角色类型定义*/
	  public static final int EventRoleAndFeature = 9;
	  
	  /** EventFeatures|动态属性类型定义*/
	  public static final int EventFeature = 10;
	  
	  /** 未知类型定义*/
	  public static final int Unknown = 0;
	  
}
