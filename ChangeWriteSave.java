package macro;

import java.util.*;

import star.common.*;
import star.base.neo.*;
import star.vis.*;
import star.flow.*;
import star.base.report.*;
import star.energy.*;
import java.io.FileWriter;
import java.io.FileOutputStream;
import java.io.IOException;

public class ChangeWriteSave extends StarMacro {

  public void execute() {
    execute0();
  }

  private void execute0() {
    double[] ArrG = {0.001, 0.004, 0.008, 0.01};
    int n = ArrG.length;

    String path = "D:\\Ajob\\sym\\PressDrop.txt";
    try (FileWriter fWriter = new FileWriter(path)) {
      fWriter.write("");
    } catch (IOException e) {
      e.printStackTrace();
    }

    for (int i = 0; i < n; i++){
      Simulation simulation_0 = getActiveSimulation();
      Solution solution_0 = simulation_0.getSolution();
      solution_0.initializeSolution();
      simulation_0.getSimulationIterator().run();

      simulation_0.saveState("D:\\Ajob\\sym\\pipe_"+ArrG[i]+".sim");

      Region region_1 = simulation_0.getRegionManager().getRegion("Body 1");
      Boundary boundary_0 = region_1.getBoundaryManager().getBoundary("inlet");
      MassFlowRateProfile massFlowRateProfile_0 = boundary_0.getValues().get(MassFlowRateProfile.class);
      Units units_2 = ((Units) simulation_0.getUnitsManager().getObject("kg/s"));
      massFlowRateProfile_0.getMethod(ConstantScalarProfileMethod.class).getQuantity().setValueAndUnits(ArrG[i], units_2);
      PressureDropReport pressureDropReport_0 = ((PressureDropReport) simulation_0.getReportManager().getReport("PrDr"));
      double pressureDropValue = pressureDropReport_0.getReportMonitorValue();

      try (FileWriter fWriter = new FileWriter(path,true)){
        fWriter.write(pressureDropValue+" ");
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
