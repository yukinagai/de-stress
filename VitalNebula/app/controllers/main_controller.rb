class MainController < ApplicationController
  def index
    @heartrates = Vital.where(device: "moto360").where("value <> 0").order("created_at asc")
  end
end
