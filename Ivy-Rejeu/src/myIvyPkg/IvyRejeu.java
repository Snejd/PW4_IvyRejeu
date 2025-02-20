/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package myIvyPkg;

import fr.dgac.ivy.IvyException;

/**
 *
 * @author vlada
 */
public class IvyRejeu
{

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            new Ex2_SimpleIvyApplication();
        } catch (IvyException ex) {
            // Print an explicit message in case of IvyException
            throw new RuntimeException("Ivy bus had a failure. Quitting Ex2_SimpleIvyApplication...");
        }
    }
    
    
}
