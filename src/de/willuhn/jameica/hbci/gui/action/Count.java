package de.willuhn.jameica.hbci.gui.action;

import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.hbci.Settings;
import de.willuhn.jameica.hbci.rmi.HBCIDBService;
import de.willuhn.jameica.hbci.rmi.Umsatz;
import de.willuhn.jameica.hbci.rmi.UmsatzTyp;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class Count implements Action
{
  /**
   * @see de.willuhn.jameica.gui.Action#handleAction(java.lang.Object)
   */
  public void handleAction(Object context) throws ApplicationException
  {
    try
    {
      HBCIDBService service = Settings.getDBService();

      DBIterator<UmsatzTyp> categories = service.createList(UmsatzTyp.class);
      Logger.info("Found " + categories.size() + " categories");
      while (categories.hasNext())
      {
        Logger.info(categories.next().getName());
      }

      DBIterator<Umsatz> transactions = service.createList(Umsatz.class);
      int transactionCount = transactions.size();
      int categorizedCount = 0;
      while (transactions.hasNext())
      {
        if (transactions.next().getUmsatzTyp() != null)
          categorizedCount++;
      }

      int categorizedPercent = transactionCount == 0 ? 0 : categorizedCount * 100 / transactionCount;
      Logger.info("Found " + transactionCount + " transactions");
      Logger.info("Of those, " + categorizedCount + " (" + categorizedPercent + "%) have a category");
    }
    catch (Exception e)
    {
      Logger.error("error while counting categories and transactions",e);
      throw new ApplicationException("Error while counting categories and transactions");
    }
  }
}
