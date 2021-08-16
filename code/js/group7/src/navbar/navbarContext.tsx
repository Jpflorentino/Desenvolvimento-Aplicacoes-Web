import React from "react";

type NavbarContextType = {
    activeItem : string,
    updateActiveItem : (newItem : string) => void
  }
  
/// Create Navbar context with default values
export const NavbarContext = React.createContext<NavbarContextType>({
    activeItem: "none",
    updateActiveItem : () => {},
});
  