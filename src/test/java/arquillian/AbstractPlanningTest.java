package arquillian;

import fr.unice.polytech.isa.dd.AvailableSlotTime;
import fr.unice.polytech.isa.dd.DeliveryRegistration;
import fr.unice.polytech.isa.dd.DeliverySchedule;
import fr.unice.polytech.isa.dd.exceptions.PackageAlreadyTookException;
import fr.unice.polytech.isa.dd.exceptions.UnvailableSlotTimeException;
import fr.unice.polytech.isa.dd.PlanningBean;
import fr.unice.polytech.isa.dd.entities.*;
import fr.unice.polytech.isa.dd.entities.Package;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import utils.MyDate;

public abstract class AbstractPlanningTest {

    @Deployment
    public static WebArchive createDeployement(){
        return ShrinkWrap.create(WebArchive.class)
                .addAsWebInfResource(EmptyAsset.INSTANCE,"beans.xml")
                .addPackage(MyDate.class.getPackage())
                .addPackage(DeliverySchedule.class.getPackage())
                .addPackage(AvailableSlotTime.class.getPackage())
                .addPackage(DeliveryRegistration.class.getPackage())
                .addPackage(PlanningBean.class.getPackage())
                .addPackage(UnvailableSlotTimeException.class.getPackage())
                .addPackage(PackageAlreadyTookException.class.getPackage())
                .addPackage(Delivery.class.getPackage())
                .addPackage(Customer.class.getPackage())
                .addPackage(Provider.class.getPackage())
                .addPackage(Database.class.getPackage())
                .addPackage(Package.class.getPackage())
                .addPackage(Bill.class.getPackage())
                .addAsManifestResource(new ClassLoaderAsset("META-INF/persistence.xml"), "persistence.xml")
                ;
    }
}
